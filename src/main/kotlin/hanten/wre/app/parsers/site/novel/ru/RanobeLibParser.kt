package hanten.wre.app.parsers.site.novel.ru

import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.model.ContentRating
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.Manga
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaListFilterOptions
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaState
import hanten.wre.app.parsers.model.MangaTag
import hanten.wre.app.parsers.model.SortOrder
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.json.getStringOrNull
import hanten.wre.app.parsers.util.json.mapJSON
import hanten.wre.app.parsers.util.NovelJsonContentParser
import hanten.wre.app.parsers.util.parseJson
import hanten.wre.app.parsers.util.toAbsoluteUrl

/**
 * RanobeLib is the novel branch of the lib.social API used by MangaLib.
 * Chapter text is served in `data.content`, so pages are HTML fragments
 * instead of image links.
 */
@MangaSourceParser("RANOBELIB", "RanobeLib", "ru", type = ContentType.NOVEL)
internal class RanobeLibParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.RANOBELIB, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("ranobelib.me")

	override val availableSortOrders: Set<SortOrder> = setOf(
		SortOrder.POPULARITY,
		SortOrder.UPDATED,
		SortOrder.NEWEST,
		SortOrder.RELEVANCE,
	)

	override val defaultSortOrder: SortOrder = SortOrder.POPULARITY

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
			isSearchWithFiltersSupported = false,
		)

	private val apiHeaders: Headers
		get() = getRequestHeaders().newBuilder()
			.add("Accept", "application/json, text/plain, */*")
			.add("Referer", "https://$domain/")
			.add("Origin", "https://$domain")
			.add("Site-Id", SITE_ID.toString())
			.build()

	private val apiUrl: String
		get() = "https://$API_HOST/api"

	override suspend fun getFilterOptions(): MangaListFilterOptions = MangaListFilterOptions()

	override suspend fun getListPage(
		page: Int,
		order: SortOrder,
		filter: MangaListFilter,
	): List<Manga> {
		val url = "$apiUrl/manga".toHttpUrl().newBuilder()
			.addQueryParameter("site_id[]", SITE_ID.toString())
			.addQueryParameter("fields[]", "rate")
			.addQueryParameter("fields[]", "rate_avg")
			.addQueryParameter("page", page.toString())
			.addQueryParameter("limit", PAGE_SIZE.toString())
			.apply {
				filter.query?.trim()?.takeIf { it.isNotEmpty() }?.let { addQueryParameter("q", it) }
				when (order) {
					SortOrder.UPDATED -> {
						addQueryParameter("sort_by", "last_chapter_at")
						addQueryParameter("sort_type", "desc")
					}

					SortOrder.NEWEST -> {
						addQueryParameter("sort_by", "created_at")
						addQueryParameter("sort_type", "desc")
					}

					SortOrder.POPULARITY -> {
						addQueryParameter("sort_by", "views")
						addQueryParameter("sort_type", "desc")
					}

					else -> Unit
				}
			}
			.build()
		val data = webClient.httpGet(url, apiHeaders).parseJson().optJSONArray("data")
			?: return emptyList()
		return data.mapJSON { jo -> parseManga(jo) }
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val url = "$apiUrl/manga/${manga.url}".toHttpUrl().newBuilder()
			.addQueryParameter("fields[]", "summary")
			.addQueryParameter("fields[]", "genres")
			.addQueryParameter("fields[]", "tags")
			.addQueryParameter("fields[]", "authors")
			.build()
		val data = webClient.httpGet(url, apiHeaders).parseJson().optJSONObject("data")
			?: return manga
		val cover = data.optJSONObject("cover")
		val summary = data.getStringOrNull("summary")
		return manga.copy(
			title = data.getStringOrNull("rus_name") ?: manga.title,
			altTitles = setOfNotNull(data.getStringOrNull("name")),
			rating = data.optJSONObject("rating")?.getStringOrNull("average")
				?.toFloatOrNull()?.div(10f) ?: manga.rating,
			coverUrl = cover?.getStringOrNull("thumbnail") ?: manga.coverUrl,
			largeCoverUrl = cover?.getStringOrNull("default") ?: manga.largeCoverUrl,
			description = summary?.let { NovelJsonContentParser.toPlainText(it, "https://$domain") ?: it },
			tags = parseTags(data),
			authors = data.optJSONArray("authors")?.mapJSON { it.getStringOrNull("name") }
				?.filterNotNull()?.toSet().orEmpty(),
			state = parseState(data),
			chapters = fetchChapters(manga.url),
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val url = "$apiUrl/manga/${chapter.url}".toHttpUrl().newBuilder()
			.build()
		val data = webClient.httpGet(url, apiHeaders).parseJson().optJSONObject("data")
			?: return emptyList()
		val content = data.getStringOrNull("content")
			?: data.getStringOrNull("body")
			?: return emptyList()
		val html = NovelJsonContentParser.toHtml(content, data.opt("attachments"), "https://$domain")
		if (html.isBlank()) {
			return emptyList()
		}
		return listOf(
			MangaPage(
				id = generateUid(chapter.url),
				url = html,
				preview = null,
				source = source,
			),
		)
	}

	private suspend fun fetchChapters(slug: String): List<MangaChapter> {
		val data = webClient.httpGet("$apiUrl/manga/$slug/chapters", apiHeaders)
			.parseJson()
			.optJSONArray("data")
			?: return emptyList()
		val result = ArrayList<MangaChapter>(data.length())
		for (i in 0 until data.length()) {
			val jo = data.optJSONObject(i) ?: continue
			val number = jo.getStringOrNull("number")?.toFloatOrNull() ?: 0f
			val volume = jo.getStringOrNull("volume")?.toIntOrNull() ?: 0
			val name = jo.getStringOrNull("name")
			val branches = jo.optJSONArray("branches") ?: continue
			for (j in 0 until branches.length()) {
				val branch = branches.optJSONObject(j) ?: continue
				val id = branch.optLong("id")
				if (id == 0L) continue
				val branchId = branch.optLong("branch_id")
				val team = branch.optJSONArray("teams")?.optJSONObject(0)?.getStringOrNull("name")
				val chapterUrl = buildString {
					append(slug)
					append("/chapter?number=")
					append(if (number % 1f == 0f) number.toInt().toString() else number.toString())
					append("&volume=")
					append(volume)
					if (branchId != 0L) {
						append("&branch_id=")
						append(branchId)
					}
				}
				result += MangaChapter(
					id = id,
					title = name,
					number = number,
					volume = volume,
					url = chapterUrl,
					uploadDate = 0L,
					source = source,
					scanlator = team,
					branch = team,
				)
			}
		}
		return result.sortedBy { it.number }
	}

	private fun parseManga(jo: org.json.JSONObject): Manga {
		val slug = jo.getStringOrNull("slug_url") ?: jo.getStringOrNull("slug") ?: ""
		val cover = jo.optJSONObject("cover")
		val rating = jo.optJSONObject("rating")?.getStringOrNull("average")?.toFloatOrNull()?.div(10f)
			?: RATING_UNKNOWN
		val isAdult = (jo.optJSONObject("ageRestriction")?.optInt("id", 0) ?: 0) >= 3
		return Manga(
			id = generateUid(slug),
			title = jo.getStringOrNull("rus_name") ?: jo.getStringOrNull("name") ?: slug,
			altTitles = setOfNotNull(jo.getStringOrNull("name")),
			url = slug,
			publicUrl = "https://$domain/ru/ranobe/$slug",
			rating = rating,
			contentRating = if (isAdult) ContentRating.ADULT else null,
			coverUrl = cover?.getStringOrNull("thumbnail"),
			tags = emptySet(),
			state = parseState(jo),
			authors = emptySet(),
			largeCoverUrl = cover?.getStringOrNull("default"),
			source = source,
		)
	}

	private fun parseState(jo: org.json.JSONObject): MangaState? = when (jo.optJSONObject("status")?.optInt("id")) {
		1 -> MangaState.ONGOING
		2 -> MangaState.FINISHED
		3 -> MangaState.UPCOMING
		4 -> MangaState.PAUSED
		5 -> MangaState.ABANDONED
		else -> null
	}

	private fun parseTags(jo: org.json.JSONObject): Set<MangaTag> {
		val result = LinkedHashSet<MangaTag>()
		for (key in arrayOf("genres", "tags")) {
			val array = jo.optJSONArray(key) ?: continue
			for (i in 0 until array.length()) {
				val tag = array.optJSONObject(i) ?: continue
				val title = tag.getStringOrNull("name") ?: continue
				result += MangaTag(
					title = title,
					key = "${key.first()}${tag.optInt("id")}",
					source = source,
				)
			}
		}
		return result
	}

	private companion object {
		const val API_HOST = "api.cdnlibs.org"
		const val SITE_ID = 3
		const val PAGE_SIZE = 20
		const val RATING_UNKNOWN = -1f
	}
}
