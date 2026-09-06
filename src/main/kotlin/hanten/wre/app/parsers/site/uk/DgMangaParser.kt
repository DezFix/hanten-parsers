package hanten.wre.app.parsers.site.uk

import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.*
import java.text.SimpleDateFormat
import java.util.*

private const val PAGE_SIZE = 20
private const val CHAPTERS_LIMIT = 500

@MangaSourceParser("DGMANGA", "DgManga", "uk")
internal class DgMangaParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.DGMANGA, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("dgmanga.app")

	// The API exposes no sort parameters; the catalog comes in the site default order.
	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	private fun apiUrl() = "https://dgmanga.app/api"

	private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val url = buildString {
			append(apiUrl())
			append("/titles?limit=")
			append(pageSize)
			append("&page=")
			append(page)
			filter.query?.takeIf { it.isNotEmpty() }?.let {
				append("&q=")
				append(it.urlEncoded())
			}
		}
		return webClient.httpGet(url).parseJson().getJSONArray("titles").mapJSON(::parseManga)
	}

	private fun parseManga(jo: JSONObject): Manga {
		val id = jo.getString("_id")
		return Manga(
			id = generateUid(id),
			url = id,
			publicUrl = "https://${domain}/title/$id",
			title = jo.getString("title"),
			altTitles = setOfNotNull(jo.getStringOrNull("originalTitle")),
			coverUrl = jo.getStringOrNull("cover"),
			tags = jo.optJSONArray("genres")?.asTypedList<String>()?.mapNotNullToSet { name ->
				name.takeUnless { it.isEmpty() }?.let {
					MangaTag(
						title = it.toTitleCase(sourceLocale),
						key = it,
						source = source,
					)
				}
			}.orEmpty(),
			authors = buildSet {
				jo.optJSONArray("authorRef")?.asTypedList<JSONObject>()?.mapNotNullTo(this) {
					it.getStringOrNull("name")
				}
				jo.optJSONArray("illustratorRef")?.asTypedList<JSONObject>()?.mapNotNullTo(this) {
					it.getStringOrNull("name")
				}
			},
			state = when (jo.getStringOrNull("title_status")) {
				"Завершено" -> MangaState.FINISHED
				"Призупинено" -> MangaState.PAUSED
				else -> MangaState.ONGOING
			},
			contentRating = when (jo.getStringOrNull("ageRating")) {
				"18+" -> ContentRating.ADULT
				"16+" -> ContentRating.SUGGESTIVE
				else -> ContentRating.SAFE
			},
			description = jo.getStringOrNull("description"),
			rating = RATING_UNKNOWN,
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		// The API has no title-details endpoint; list items already carry the full
		// metadata, so only the chapter list is fetched here.
		val chaptersJson = webClient.httpGet(
			"${apiUrl()}/chapters/title/${manga.url}?limit=$CHAPTERS_LIMIT",
		).parseJsonArray()
		return manga.copy(
			chapters = chaptersJson.asTypedList<JSONObject>().mapChapters { _, cjo ->
				MangaChapter(
					id = generateUid(cjo.getString("_id")),
					url = cjo.getString("_id"),
					title = cjo.getStringOrNull("chapterName"),
					number = cjo.getDoubleOrDefault("chapterNumber", 0.0).toFloat(),
					volume = cjo.getIntOrDefault("volumeNumber", 0),
					uploadDate = dateFormat.parseSafe(cjo.getStringOrNull("createdAt")),
					scanlator = null,
					branch = null,
					source = source,
				)
			},
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		return webClient.httpGet("${apiUrl()}/chapters/${chapter.url}")
			.parseJson()
			.getJSONArray("pages")
			.asTypedList<String>()
			.map { url ->
				MangaPage(
					id = generateUid(url),
					url = url,
					preview = null,
					source = source,
				)
			}
	}
}
