package hanten.wre.app.parsers.site.en

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import java.util.*

private const val LANG = "en"
private const val CHAPTERS_ORDER = "asc"

@MangaSourceParser("MANGADDICT", "Mangaddict", "en")
internal class MangaddictParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.MANGADDICT, 24) {

	override val configKeyDomain = ConfigKey.Domain("www.mangaddict.com", "mangaddict.com")

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val query = filter.query?.takeIf { it.isNotEmpty() }
		if (page == 1) {
			val url = if (query != null) {
				"https://$domain/$LANG/?s=${query.urlEncoded()}"
			} else {
				"https://$domain/$LANG/"
			}
			val doc = webClient.httpGet(url).parseHtml()
			return doc.select("a.series-card-link[href]").mapNotNull(::parseCard)
		}
		val nonce = fetchNonce(
			if (query != null) "https://$domain/$LANG/?s=${query.urlEncoded()}" else "https://$domain/$LANG/",
		)
		val form = buildMap {
			put("action", "mangaverse_load_more")
			put("nonce", nonce)
			put("page", page.toString())
			put("lang", LANG)
			if (query != null) {
				put("type", "search")
				put("search_query", query)
			} else {
				put("type", "series_grid")
			}
		}
		val html = postAjax(form)
		return Jsoup.parseBodyFragment(html).select("a.series-card-link[href]").mapNotNull(::parseCard)
	}

	private fun parseCard(a: Element): Manga? {
		// AJAX fragments have no base URI, so relativize against any host manually
		val href = a.relHref()
		if (!href.contains("/manga/")) {
			return null
		}
		val title = a.selectFirst(".series-card-title")?.text()?.trim()
			?: a.selectFirst("img[alt]")?.attr("alt")?.trim()?.takeUnless { it.isEmpty() }
			?: return null
		val cover = a.selectFirst(".series-card-thumb")?.let { thumb ->
			coverFromStyle(thumb.attr("style"))
		} ?: a.selectFirst("img[src]")?.attrAsAbsoluteUrlOrNull("src")
		return Manga(
			id = generateUid(href),
			url = href,
			publicUrl = a.attrAsAbsoluteUrl("href"),
			title = title,
			altTitles = emptySet(),
			authors = emptySet(),
			description = null,
			tags = emptySet(),
			rating = RATING_UNKNOWN,
			state = null,
			coverUrl = cover,
			contentRating = null,
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain)).parseHtml()
		val title = doc.selectFirst("h1.series-title")?.text()?.trim()?.takeUnless { it.isEmpty() }
			?: manga.title
		val description = doc.selectFirst(".series-description")?.text()?.trim()
		val cover = doc.selectFirst(".series-header-thumbnail img")?.attrAsAbsoluteUrlOrNull("src")
			?: manga.coverUrl
		val categoryId = doc.selectFirst(".chapters-list[data-category]")?.attr("data-category")
			?: throw ParseException("Chapter list not found", manga.url)
		val nonce = doc.extractNonce()
		return manga.copy(
			title = title,
			description = description,
			coverUrl = cover,
			chapters = fetchAllChapters(categoryId, nonce).mapChapters(reversed = false) { _, cjo ->
				parseChapter(cjo)
			},
		)
	}

	private suspend fun fetchAllChapters(categoryId: String, nonce: String): List<Element> {
		val result = ArrayList<Element>()
		var page = 1
		while (page <= MAX_CHAPTER_PAGES) {
			val form = mapOf(
				"action" to "mangaverse_load_more",
				"nonce" to nonce,
				"page" to page.toString(),
				"type" to "series",
				"category_id" to categoryId,
				"order" to CHAPTERS_ORDER,
				"lang" to LANG,
			)
			val (html, hasMore) = postAjaxWithFlag(form)
			if (html.isNotEmpty()) {
				result += Jsoup.parseBodyFragment(html).select("a.chapter-link[href]")
			}
			if (!hasMore) {
				break
			}
			page++
		}
		return result
	}

	private fun parseChapter(a: Element): MangaChapter? {
		val href = a.relHref()
		val title = a.selectFirst(".chapter-title")?.text()?.trim() ?: a.text().trim()
		val number = chapterNumberRegex.find(title)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
		return MangaChapter(
			id = generateUid(href),
			url = href,
			title = title.takeUnless { it.isEmpty() },
			number = number,
			volume = 0,
			uploadDate = 0L,
			scanlator = null,
			branch = null,
			source = source,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain)).parseHtml()
		return doc.select("figure.wp-block-image img").mapNotNull { img ->
			val url = img.attrAsAbsoluteUrlOrNull("data-src")
				?: img.attrAsAbsoluteUrlOrNull("src")
				?: return@mapNotNull null
			if (url.startsWith("data:")) {
				return@mapNotNull null
			}
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}.ifEmpty { throw ParseException("No pages found", chapter.url) }
	}

	private suspend fun fetchNonce(pageUrl: String): String {
		val doc = webClient.httpGet(pageUrl).parseHtml()
		return doc.extractNonce()
	}

	private fun Document.extractNonce(): String {
		val script = select("script").firstNotNullOfOrNull { it.data().takeIf { d -> "mangaverse_ajax" in d } }
			?: parseFailed("Nonce script not found")
		return nonceRegex.find(script)?.groupValues?.get(1)
			?: parseFailed("Nonce not found")
	}

	private suspend fun postAjax(form: Map<String, String>): String {
		return postAjaxWithFlag(form).first
	}

	private suspend fun postAjaxWithFlag(form: Map<String, String>): Pair<String, Boolean> {
		val json = webClient.httpPost("https://$domain/wp-admin/admin-ajax.php".toHttpUrl(), form).parseJson()
		if (!json.optBoolean("success", false)) {
			throw ParseException("AJAX request failed", form.toString())
		}
		val data = json.getJSONObject("data")
		return data.optString("html", "") to data.optBoolean("has_more", false)
	}

	private fun Element.relHref(): String {
		val raw = attr("href").trim()
		if (raw.startsWith('/')) {
			return raw
		}
		val path = raw.substringAfter("://", raw).substringAfter("/", "")
		return if (path.isEmpty()) raw else "/$path"
	}

	private fun coverFromStyle(style: String): String? {
		val url = coverStyleRegex.find(style)?.groupValues?.get(1) ?: return null
		return url.toAbsoluteUrl(domain)
	}

	private companion object {

		const val MAX_CHAPTER_PAGES = 50

		val chapterNumberRegex = Regex("Chapter\\s+([\\d.]+)", RegexOption.IGNORE_CASE)
		val nonceRegex = Regex(""""nonce"\s*:\s*"([0-9a-f]+)"""")
		val coverStyleRegex = Regex("""url\(['"]?([^'")]+)['"]?\)""")
	}
}
