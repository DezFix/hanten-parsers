package hanten.wre.app.parsers.site.ru

import androidx.collection.ArrayMap
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.json.JSONObject
import hanten.wre.app.parsers.InternalParsersApi
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.*
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("MANGA_WTF", "MangaWtf", "ru")
internal class MangaWtfParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.MANGA_WTF, pageSize = 20) {

	override val availableSortOrders: Set<SortOrder> =
		EnumSet.of(
			SortOrder.POPULARITY,
		)

	@InternalParsersApi
	override val configKeyDomain = ConfigKey.Domain("inkstory.net", "manga.wtf")

	// Backend moved: api.inkstory.net is dead, live API is api.inuko.me.
	// /v2/books list, /label tags and /book related are gone; the list comes
	// from the server-rendered catalog instead.
	private fun apiUrl() = HttpUrl.Builder().scheme(SCHEME_HTTPS).host(API_HOST)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	init {
		paginator.firstPage = 0
		searchPaginator.firstPage = 0
	}

	companion object {

		private const val API_HOST = "api.inuko.me"

		private val CONTENT_HREF_REGEX = Regex("^/content/[a-z0-9-]+$")

		private val BOOK_ID_REGEX = Regex("\\{id:\"([0-9a-f-]{36})\"")
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		// The /v2/books list endpoint is gone; the list comes from the
		// server-rendered catalog (?page=N renders content page N+1).
		// Only pagination and text search are supported this way.
		val urlBuilder = "https://$domain/content".toHttpUrl().newBuilder()
			.addQueryParameter("page", page.toString())
		filter.query?.takeIf { it.isNotBlank() }?.let {
			urlBuilder.addQueryParameter("search", it)
		}
		val doc = webClient.httpGet(urlBuilder.build()).parseHtml()
		val result = ArrayList<Manga>()
		val seen = HashSet<String>()
		for (a in doc.select("a[href]")) {
			val href = a.attr("href")
			if (!CONTENT_HREF_REGEX.matches(href) || !seen.add(href)) {
				continue
			}
			// Cards carry a poster image; nav/service links don't
			val img = a.selectFirst("img") ?: continue
			val title = img.attr("alt").trim().takeUnless { it.isEmpty() }
				?: a.text().trim().takeUnless { it.isEmpty() }
				?: continue
			val cover = img.attr("src").trim().takeUnless { it.isEmpty() }
			val publicUrl = href.toAbsoluteUrl(domain)
			result.add(
				Manga(
					id = generateUid(publicUrl),
					url = href,
					publicUrl = publicUrl,
					title = title,
					altTitles = emptySet(),
					coverUrl = cover,
					source = source,
					rating = RATING_UNKNOWN,
					state = null,
					contentRating = null,
					tags = emptySet(),
					authors = emptySet(),
				),
			)
		}
		return result
	}

	override suspend fun getDetails(manga: Manga): Manga =
		coroutineScope {
			// HTML catalog items carry /content/{slug} urls; the API works with book ids
			val bookId = manga.url.takeUnless { it.startsWith("/content/") }
				?: resolveBookId(manga.url.substringAfterLast('/'))
			val chaptersDeferred = async { getChapters(bookId) }
			val url =
				apiUrl()
					.addPathSegment("v2")
					.addPathSegment("books")
					.addPathSegment(bookId)
			val jo = webClient.httpGet(url.build()).parseJson()
			val isNsfwSource = jo.getStringOrNull("contentStatus").isNsfw()
			Manga(
				id = generateUid(jo.getString("id")),
				title = jo.getJSONObject("name").getString("ru"),
				altTitles = setOfNotNull(jo.getJSONObject("name").getStringOrNull("en")),
				url = jo.getString("id"),
				publicUrl = "https://$domain/content/${jo.getString("slug")}",
				rating = jo.getFloatOrDefault("averageRating", -10f) / 10f,
				contentRating = if (isNsfwSource) ContentRating.ADULT else null,
				coverUrl = jo.getString("poster"),
				tags = jo.getJSONArray("labels").mapJSONToSet { it.toMangaTag() },
				state = jo.getStringOrNull("status")?.toMangaState(),
				authors = jo.getJSONArray("relations").asTypedList<JSONObject>().mapNotNullToSet {
					if (it.getStringOrNull("type") == "AUTHOR") {
						it.getJSONObject("publisher").getStringOrNull("name")
					} else {
						null
					}
				},
				source = source,
				largeCoverUrl = null,
				description = jo.getString("description").nl2br(),
				chapters = chaptersDeferred.await(),
			)
		}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val url =
			apiUrl()
				.addPathSegment("v2")
				.addPathSegment("chapters")
				.addPathSegment(chapter.url)
		val json = webClient.httpGet(url.build()).parseJson()
		return json.getJSONArray("pages").mapJSON { jo ->
			MangaPage(
				id = generateUid(jo.getString("id")),
				url = jo.getString("image"),
				preview = null,
				source = source,
			)
		}
	}

	override suspend fun getRelatedManga(seed: Manga): List<Manga> {
		// The /book related endpoint is gone with the old API host
		return emptyList()
	}

	private suspend fun resolveBookId(slug: String): String {
		val html = webClient.httpGet("https://$domain/content/$slug").parseHtml().html()
		return BOOK_ID_REGEX.find(html)?.groupValues?.get(1)
			?: throw ParseException("Cannot resolve book id for $slug", slug)
	}

	override suspend fun getPageUrl(page: MangaPage): String = page.url

	private suspend fun getChapters(mangaId: String): List<MangaChapter> {
		val url =
			apiUrl()
				.addPathSegment("v2")
				.addPathSegment("chapters")
				.addQueryParameter("bookId", mangaId)
		val ja = webClient.httpGet(url.build()).parseJsonArray()
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ROOT)
		val branches = ArrayMap<String, String>()
		return ja
			.mapJSON { jo ->
				val number = jo.getFloatOrDefault("number", 0f)
				val volume = jo.getIntOrDefault("volume", 0)
				val branchId = jo.getString("branchId")
				MangaChapter(
					id = generateUid(jo.getString("id")),
					title = jo.getStringOrNull("name"),
					number = number,
					volume = volume,
					url = jo.getString("id"),
					scanlator = null,
					uploadDate = dateFormat.parseSafe(jo.getString("createdAt")),
					branch = branches.getOrPut(branchId) { getBranchName(branchId) },
					source = source,
				)
			}.reversed()
	}

	private suspend fun getBranchName(id: String): String? =
		runCatchingCancellable {
			val url =
				apiUrl()
					.addPathSegment("branch")
					.addPathSegment(id)
			val json = webClient.httpGet(url.build()).parseJson()
			json.getJSONArray("publishers").mapJSONToSet { it.getStringOrNull("name") }.firstOrNull()
		}.getOrElse {
			id.substringBefore('-')
		}

	private fun String.toMangaState() =
		when (this.uppercase(Locale.ROOT)) {
			"DONE" -> MangaState.FINISHED
			"ONGOING" -> MangaState.ONGOING
			"FROZEN" -> MangaState.PAUSED
			"ANNOUNCE" -> MangaState.UPCOMING
			else -> null
		}

	private fun String?.isNsfw() =
		this.equals("EROTIC", ignoreCase = true) ||
			this.equals("PORNOGRAPHIC", ignoreCase = true)

	private fun JSONObject.toMangaTag() =
		MangaTag(
			title = getString("name").toTitleCase(sourceLocale),
			key = getString("slug"),
			source = source,
		)

}
