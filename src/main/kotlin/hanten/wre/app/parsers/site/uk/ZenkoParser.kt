package hanten.wre.app.parsers.site.uk

import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.*
import java.util.*

private const val PAGE_SIZE = 20
private const val CHAPTERS_LIMIT = 500
private const val CHAPTER_NAME_DELIMITER = "@#%&;№%#&**#!@"

@MangaSourceParser("ZENKO", "Zenko", "uk")
internal class ZenkoParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.ZENKO, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("zenko.online")

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.UPDATED,
		SortOrder.POPULARITY,
		SortOrder.RATING,
		SortOrder.NEWEST,
	)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	private fun apiUrl() = "https://api.zenko.online"

	private fun storageUrl(uuid: String) = "https://storage.zenko.online/$uuid?optimizer=image&quality=100"

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val offset = (page - 1) * pageSize
		val url = buildString {
			append(apiUrl())
			append("/titles?limit=")
			append(pageSize)
			append("&offset=")
			append(offset)
			append("&sortBy=")
			append(getSortKey(order))
			// NOTE: do not send ageLimit here: the API treats it as an exact
			// match (ageLimit=18 returns only 18+ titles), adult titles are
			// marked per-item via contentRating instead.
			append("&order=DESC")
			filter.query?.takeIf { it.isNotEmpty() }?.let {
				append("&name=")
				append(it.urlEncoded())
			}
		}
		return webClient.httpGet(url).parseJson().getJSONArray("data").mapJSON(::parseManga)
	}

	private fun parseManga(jo: JSONObject): Manga {
		val id = jo.getInt("id")
		return Manga(
			id = generateUid(id.toLong()),
			url = id.toString(),
			publicUrl = "https://${domain}/titles/$id",
			title = jo.getString("name"),
			altTitles = setOfNotNull(
				jo.getStringOrNull("originalName"),
				jo.getStringOrNull("engName"),
			),
			coverUrl = jo.getStringOrNull("coverImg")?.let(::storageUrl),
			tags = parseTags(jo),
			authors = parseAuthors(jo),
			state = parseState(jo.getStringOrNull("status")),
			contentRating = when (jo.optInt("ageLimit", 0)) {
				18 -> ContentRating.ADULT
				16 -> ContentRating.SUGGESTIVE
				else -> ContentRating.SAFE
			},
			rating = RATING_UNKNOWN,
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val jo = webClient.httpGet("${apiUrl()}/titles/${manga.url}").parseJson()
		val chaptersJson = webClient.httpGet(
			"${apiUrl()}/titles/${manga.url}/chapters?limit=$CHAPTERS_LIMIT",
		).parseJsonArray()
		return manga.copy(
			title = jo.getStringOrNull("name") ?: manga.title,
			description = jo.getStringOrNull("description"),
			tags = parseTags(jo),
			authors = parseAuthors(jo),
			state = parseState(jo.getStringOrNull("status")) ?: manga.state,
			coverUrl = jo.getStringOrNull("coverImg")?.let(::storageUrl) ?: manga.coverUrl,
			chapters = chaptersJson.asTypedList<JSONObject>().mapChapters { _, cjo ->
				parseChapter(cjo)
			},
		)
	}

	private fun parseChapter(cjo: JSONObject): MangaChapter? {
		val id = cjo.getIntOrDefault("id", 0)
		if (id == 0) {
			return null
		}
		val parts = (cjo.getStringOrNull("name") ?: "").split(CHAPTER_NAME_DELIMITER)
		return MangaChapter(
			id = generateUid(id.toLong()),
			url = id.toString(),
			title = parts.getOrNull(2)?.takeUnless { it.isEmpty() },
			number = parts.getOrNull(1)?.toFloatOrNull() ?: 0f,
			volume = parts.getOrNull(0)?.toIntOrNull() ?: 0,
			uploadDate = cjo.getLongOrDefault("createdAt", 0L).takeIf { it > 0 }?.times(1000) ?: 0L,
			scanlator = null,
			branch = null,
			source = source,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val pages = webClient.httpGet("${apiUrl()}/chapters/${chapter.url}")
			.parseJson()
			.getJSONArray("pages")
			.asTypedList<JSONObject>()
			.sortedBy { it.optInt("order", 0) }
		return pages.map { pjo ->
			val url = storageUrl(pjo.getString("content"))
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}

	private fun parseTags(jo: JSONObject): Set<MangaTag> = buildSet {
		jo.optJSONArray("genres")?.asTypedList<JSONObject>()?.mapTo(this) {
			MangaTag(
				title = it.getString("name").toTitleCase(sourceLocale),
				key = it.getString("name"),
				source = source,
			)
		}
		jo.optJSONArray("tags")?.asTypedList<JSONObject>()?.mapTo(this) {
			MangaTag(
				title = it.getString("name").toTitleCase(sourceLocale),
				key = it.getString("name"),
				source = source,
			)
		}
	}

	private fun parseAuthors(jo: JSONObject): Set<String> = buildSet {
		jo.optJSONArray("writers")?.asTypedList<JSONObject>()?.mapNotNullTo(this) {
			it.getStringOrNull("name")?.takeUnless(String::isEmpty)
		}
		jo.optJSONArray("painters")?.asTypedList<JSONObject>()?.mapNotNullTo(this) {
			it.getStringOrNull("name")?.takeUnless(String::isEmpty)
		}
	}

	private fun parseState(status: String?) = when (status) {
		"ONGOING" -> MangaState.ONGOING
		"COMPLETED", "FINISHED" -> MangaState.FINISHED
		"PAUSED" -> MangaState.PAUSED
		"ABANDONED" -> MangaState.ABANDONED
		else -> null
	}

	private fun getSortKey(order: SortOrder) = when (order) {
		SortOrder.POPULARITY -> "viewsCount"
		SortOrder.RATING -> "likesCount"
		SortOrder.NEWEST -> "createdAt"
		else -> "lastChapterCreatedAt"
	}
}
