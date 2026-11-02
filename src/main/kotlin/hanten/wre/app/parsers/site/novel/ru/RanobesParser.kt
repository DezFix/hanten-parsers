package hanten.wre.app.parsers.site.novel.ru

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
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
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.toAbsoluteUrl

/**
 * Ranobes is a DLE-based novel library with no public API:
 * `/ranobe/` is the catalog, `/popular.html` the all-time top,
 * `/ranobe/{id}-{slug}.html` the novel page and `/chapters/{slug}/page/{n}/`
 * the paginated chapter list. Text lives in `#arrticle`.
 *
 * There is no server-side title search, so the parser reports search as
 * unsupported and the app filters the catalog page locally.
 */
@MangaSourceParser("RANOBES", "Ranobes", "ru", type = ContentType.NOVEL)
internal class RanobesParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.RANOBES, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("ranobes.com")

	override val availableSortOrders: Set<SortOrder> = setOf(
		SortOrder.NEWEST,
		SortOrder.POPULARITY,
	)

	override val defaultSortOrder: SortOrder = SortOrder.NEWEST

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = false,
			isSearchWithFiltersSupported = false,
		)

	override suspend fun getFilterOptions(): MangaListFilterOptions = MangaListFilterOptions()

	override suspend fun getListPage(
		page: Int,
		order: SortOrder,
		filter: MangaListFilter,
	): List<Manga> {
		val url = when {
			page > 1 -> "https://$domain/ranobe/page/$page/"
			order == SortOrder.POPULARITY -> "https://$domain/popular.html"
			else -> "https://$domain/ranobe/"
		}
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val result = LinkedHashMap<Long, Manga>()
		for (article in doc.select("article")) {
			val manga = parseListItem(article) ?: continue
			result.putIfAbsent(manga.id, manga)
		}
		return result.values.toList()
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val url = manga.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val poster = doc.selectFirst(".r-fullstory-poster, .poster")
		val cover = poster?.let(::parseCoverUrl) ?: manga.coverUrl
		val description = doc.selectFirst("#fs-info .r-desription .cont-text, .r-desription .cont-text, #fs-info")
			?.text()?.trim()?.takeIf { it.isNotEmpty() }
		val authors = doc.select("a[href*=/cloud/authors/], a[href*=/cloud/translater/]")
			.map { it.text().trim() }
			.filterTo(LinkedHashSet()) { it.isNotEmpty() }
		val title = doc.selectFirst("h1.title")?.ownText()?.trim()?.takeIf { it.isNotEmpty() }
		return manga.copy(
			title = title ?: manga.title,
			description = description,
			coverUrl = cover,
			largeCoverUrl = cover,
			authors = authors.ifEmpty { manga.authors },
			chapters = fetchChapters(manga.url),
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val url = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val content = doc.selectFirst("#arrticle, #article, .entry-content") ?: doc.body()
		content.select("script, style, iframe, .ads, .share, .free-support-top, [id^=bg-ssp]").remove()
		val html = content.html()
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

	private fun parseListItem(article: Element): Manga? {
		val link = article.select("a[href]").firstOrNull {
			NOVEL_PATH.matches(it.attr("href")) || NOVEL_PATH.matches(it.absUrl("href"))
		} ?: return null
		val href = link.absUrl("href").ifBlank { link.attr("href").toAbsoluteUrl(domain) }
		val title = link.selectFirst(".title-limit")?.text()?.trim()
			?.takeIf { it.isNotEmpty() }
			?: link.text().trim().takeIf { it.isNotEmpty() }
			?: article.selectFirst("img[alt]")?.attr("alt")?.trim()?.takeIf { it.isNotEmpty() }
			?: return null
		val cover = parseCoverUrl(article)
		val description = article.selectFirst(".text, .moreless__short, .moreless__full")
			?.text()?.trim()?.takeIf { it.isNotEmpty() }
		return Manga(
			id = generateUid(href),
			title = title,
			altTitles = emptySet(),
			url = href,
			publicUrl = href,
			rating = parseRating(article),
			contentRating = null,
			coverUrl = cover,
			tags = emptySet<MangaTag>(),
			state = null,
			authors = emptySet(),
			largeCoverUrl = cover,
			description = description,
			chapters = null,
			source = source,
		)
	}

	private fun parseRating(article: Element): Float {
		val style = article.selectFirst(".current-rating")?.attr("style").orEmpty()
		val percent = Regex("""width:\s*(\d+)%""").find(style)?.groupValues?.get(1)?.toIntOrNull()
			?: return RATING_UNKNOWN
		return (percent / 100f).coerceIn(0f, 1f)
	}

	private fun parseCoverUrl(element: Element): String? {
		val img = element.selectFirst("img[src]")
		val src = img?.absUrl("src")?.ifEmpty { img.attr("src") }
		if (!src.isNullOrEmpty() && !src.startsWith("data:")) {
			return normalizeCover(src.toAbsoluteUrl(domain))
		}
		val style = element.selectFirst("[style*=background-image], [style*=background]")?.attr("style")
			.orEmpty()
		val match = Regex("""url\(['"]?(.*?)['"]?\)""").find(style) ?: return null
		return normalizeCover(match.groupValues[1].toAbsoluteUrl(domain))
	}

	private fun normalizeCover(url: String) = url.replace("/thumbs/", "/")

	private suspend fun fetchChapters(novelUrl: String): List<MangaChapter> {
		val slug = novelUrl.substringAfterLast('/')
			.removeSuffix(".html")
			.substringAfter('-')
		if (slug.isEmpty()) {
			return emptyList()
		}
		val chaptersUrl = "https://$domain/chapters/$slug/"
		val firstPage = runCatching { webClient.httpGet(chaptersUrl, getRequestHeaders()).parseHtml() }.getOrNull()
			?: return emptyList()
		val lastPage = firstPage.select("a[href*=/page/]")
			.mapNotNull { Regex("""/page/(\d+)/""").find(it.attr("href"))?.groupValues?.get(1)?.toIntOrNull() }
			.maxOrNull()
			?.coerceAtMost(MAX_CHAPTER_PAGES)
			?: 1
		val pages = coroutineScope {
			listOf(firstPage) + (2..lastPage).map { page ->
				async(Dispatchers.IO) {
					runCatching { webClient.httpGet("${chaptersUrl}page/$page/", getRequestHeaders()).parseHtml() }.getOrNull()
				}
			}.awaitAll()
		}
		val raw = ArrayList<Pair<String, String>>()
		for (doc in pages.filterNotNull()) {
			for (item in doc.select("#dle-content .cat_line, .cat_block.cat_line")) {
				val link = item.selectFirst("a[href]") ?: continue
				val href = link.absUrl("href").ifBlank { link.attr("href").toAbsoluteUrl(domain) }
				val title = link.selectFirst(".title")?.text()?.trim()
					?: link.attr("title").trim()
					?: continue
				raw += href to title
			}
		}
		return raw.asReversed()
			.distinctBy { it.first }
			.mapIndexed { index, (href, title) ->
				MangaChapter(
					id = generateUid(href),
					title = title,
					number = CHAPTER_NUMBER.find(title)?.groupValues?.get(1)
						?.replace(',', '.')?.toFloatOrNull() ?: (index + 1).toFloat(),
					volume = 0,
					url = href,
					uploadDate = 0L,
					source = source,
					scanlator = null,
					branch = null,
				)
			}
	}

	private companion object {
		const val PAGE_SIZE = 10
		const val MAX_CHAPTER_PAGES = 60
		const val RATING_UNKNOWN = -1f
		val NOVEL_PATH = Regex("""(?:https?://ranobes\.(?:com|net|top))?/ranobe/\d+-[^/]+\.html""")
		val CHAPTER_NUMBER = Regex("""(?i)глав[аы]?\s*(\d+(?:[.,]\d+)?)""")
	}
}
