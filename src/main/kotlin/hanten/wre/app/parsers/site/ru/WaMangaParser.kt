package hanten.wre.app.parsers.site.ru

import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.*
import kotlinx.coroutines.delay
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("WAMANGA", "WaManga", "ru", type = ContentType.MANGA)
internal class WaMangaParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.WAMANGA, pageSize = 20) {

	override val configKeyDomain = ConfigKey.Domain("wamanga.ru")

	// The API returns the catalog in alphabetical order; no server-side sorting.
	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.ALPHABETICAL)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	private fun apiUrl() = "https://${domain}/api/v1"

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val offset = (page - 1) * pageSize
		val url = buildString {
			append(apiUrl())
			append("/manga?limit=")
			append(pageSize)
			append("&offset=")
			append(offset)
			filter.query?.takeIf { it.isNotEmpty() }?.let {
				append("&query=")
				append(it.urlEncoded())
			}
		}
		return retryIO { webClient.httpGet(url).parseJsonArray() }.mapJSON { parseManga(it) }
	}

	private fun parseManga(jo: JSONObject): Manga {
		val id = jo.getString("id")
		val slug = jo.getString("slug")
		return Manga(
			id = generateUid(id),
			url = id,
			publicUrl = "https://${domain}/manga/$slug",
			title = jo.getString("title"),
			altTitles = collectAltTitles(jo),
			coverUrl = jo.getStringOrNull("coverUrl")?.toAbsoluteUrl(domain),
			largeCoverUrl = jo.getStringOrNull("imageUrl")?.toAbsoluteUrl(domain),
			tags = parseTags(jo),
			state = parseState(jo.getStringOrNull("statusTitle")),
			authors = jo.optJSONArray("authors")?.asTypedList<String>()?.toSet().orEmpty(),
			rating = parseRating(jo),
			contentRating = if (jo.getBooleanOrDefault("isAdult", false)) {
				ContentRating.ADULT
			} else {
				ContentRating.SAFE
			},
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val jo = retryIO { webClient.httpGet("${apiUrl()}/manga/${manga.url}").parseJson() }
		val chaptersJson = retryIO {
			webClient.httpGet("${apiUrl()}/manga/${manga.url}/chapters").parseJsonArray()
		}
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", sourceLocale)
		val scanlator = jo.optJSONArray("teams")
			?.mapJSON { it.getStringOrNull("name") }
			?.filterNotNull()
			?.joinToString()
			?.takeUnless { it.isEmpty() }
		return manga.copy(
			altTitles = collectAltTitles(jo),
			description = jo.getStringOrNull("description"),
			tags = parseTags(jo),
			authors = jo.optJSONArray("authors")?.asTypedList<String>()?.toSet().orEmpty(),
			state = parseState(jo.getStringOrNull("statusTitle")),
			largeCoverUrl = jo.getStringOrNull("imageUrl")?.toAbsoluteUrl(domain) ?: manga.largeCoverUrl,
			chapters = chaptersJson.asTypedList<JSONObject>().mapChapters { _, it ->
				val chapterId = it.getString("id")
				val position = it.getFloatOrDefault("position", 0f)
				MangaChapter(
					id = generateUid(chapterId),
					url = chapterId,
					title = it.getStringOrNull("title")?.takeUnless { t -> t.isEmpty() },
					number = position,
					volume = 0,
					scanlator = scanlator,
					uploadDate = dateFormat.parseSafe(it.getStringOrNull("createdAt")),
					branch = null,
					source = source,
				)
			},
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		return retryIO {
			webClient.httpGet("${apiUrl()}/chapters/${chapter.url}")
				.parseJson()
				.getJSONArray("files")
		}.mapJSON { file ->
				val img = file.getString("diskFile").toAbsoluteUrl(domain)
				MangaPage(
					id = generateUid(img),
					url = img,
					preview = null,
					source = source,
				)
			}
	}

	private fun collectAltTitles(jo: JSONObject): Set<String> = buildSet {
		jo.getStringOrNull("titleEnglish")?.takeUnless { it.isEmpty() }?.let(::add)
		jo.optJSONArray("alternateTitles")?.asTypedList<String>()?.forEach { if (it.isNotEmpty()) add(it) }
	}

	private fun parseTags(jo: JSONObject): Set<MangaTag> {
		return jo.optJSONArray("genres")?.asTypedList<String>()?.mapNotNullToSet { name ->
			if (name.isEmpty()) {
				null
			} else {
				MangaTag(
					title = name.toTitleCase(sourceLocale),
					key = name,
					source = source,
				)
			}
		}.orEmpty()
	}

	private fun parseRating(jo: JSONObject): Float {
		// The API has no documented rating field; TomiloLib (similar RU API)
		// uses averageRating out of 10, so that scale is assumed here.
		// Anything unrecognized falls back to unknown.
		jo.optDouble("averageRating", Double.NaN).takeIf { it.isFinite() && it > 0 }?.let {
			return (it / 10.0).toFloat().coerceIn(0f, 1f)
		}
		jo.optDouble("rating", Double.NaN).takeIf { it.isFinite() && it > 0 }?.let {
			return (it / 10.0).toFloat().coerceIn(0f, 1f)
		}
		jo.optDouble("score", Double.NaN).takeIf { it.isFinite() && it > 0 }?.let {
			return (it / 10.0).toFloat().coerceIn(0f, 1f)
		}
		jo.optJSONObject("rating")?.let { nested ->
			listOf("average", "score", "value").firstNotNullOfOrNull { key ->
				nested.optDouble(key, Double.NaN).takeIf { it.isFinite() && it > 0 }
			}?.let { return (it / 10.0).toFloat().coerceIn(0f, 1f) }
		}
		return RATING_UNKNOWN
	}

	private fun parseState(status: String?) = when (status?.lowercase(sourceLocale)) {
		"ongoing" -> MangaState.ONGOING
		"finished", "completed" -> MangaState.FINISHED
		"abandoned" -> MangaState.ABANDONED
		"paused", "frozen" -> MangaState.PAUSED
		else -> null
	}

	// The host stalls intermittently; retry network failures once after a short pause.
	private suspend fun <T> retryIO(times: Int = 2, block: suspend () -> T): T {
		var error: IOException? = null
		repeat(times) { attempt ->
			try {
				return block()
			} catch (e: kotlinx.coroutines.CancellationException) {
				throw e
			} catch (e: IOException) {
				error = e
				if (attempt + 1 < times) {
					delay(1000)
				}
			}
		}
		throw error!!
	}
}
