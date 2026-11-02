package hanten.wre.app.parsers.site.novel.uk

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
import hanten.wre.app.parsers.model.SortOrder
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.toAbsoluteUrl

/**
 * Baka.in.ua is a Ukrainian fan-translation library without a public API.
 * `/fictions` renders the catalog, `/fictions/{slug}` the novel page and
 * `/chapters/{slug}` the chapter text. Only the first catalog page is used:
 * the site renders the whole list at once.
 */
@MangaSourceParser("BAKA_UA", "Baka.in.ua", "uk", type = ContentType.NOVEL)
internal class BakaUaParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.BAKA_UA, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("baka.in.ua")

	override val availableSortOrders: Set<SortOrder> = setOf(
		SortOrder.POPULARITY,
		SortOrder.UPDATED,
		SortOrder.NEWEST,
	)

	override val defaultSortOrder: SortOrder = SortOrder.UPDATED

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
			isSearchWithFiltersSupported = false,
		)

	override suspend fun getFilterOptions(): MangaListFilterOptions = MangaListFilterOptions()

	override suspend fun getListPage(
		page: Int,
		order: SortOrder,
		filter: MangaListFilter,
	): List<Manga> {
		// Baka renders the whole catalog on /fictions; extra pages are empty.
		if (page > 1) {
			return emptyList()
		}
		val doc = webClient.httpGet("https://$domain/fictions", getRequestHeaders()).parseHtml()
		val query = filter.query?.trim()
		val result = LinkedHashMap<Long, Manga>()
		for (article in doc.select("article")) {
			val link = article.selectFirst("p a[href^=/fictions/]")
				?: article.selectFirst("a[href^=/fictions/]")
				?: continue
			val href = link.attr("href")
			if (href.contains("/fictions/genres/") || href.contains("/fictions/calendar")) {
				continue
			}
			val title = link.text().trim().ifBlank {
				article.selectFirst("img[alt]")?.attr("alt")?.trim().orEmpty()
			}
			if (title.isBlank() || (!query.isNullOrEmpty() && !title.contains(query, ignoreCase = true))) {
				continue
			}
			val absoluteUrl = href.toAbsoluteUrl(domain)
			val cover = article.selectFirst("img[src]")?.let { img ->
				img.absUrl("src").ifBlank { img.attr("src") }.toAbsoluteUrl(domain)
			}?.takeIf { it.isNotBlank() && !it.startsWith("data:") }
			val manga = Manga(
				id = generateUid(absoluteUrl),
				title = title,
				altTitles = emptySet(),
				url = absoluteUrl,
				publicUrl = absoluteUrl,
				rating = RATING_UNKNOWN,
				contentRating = null,
				coverUrl = cover,
				tags = emptySet(),
				state = null,
				authors = emptySet(),
				largeCoverUrl = cover,
				source = source,
			)
			result.putIfAbsent(manga.id, manga)
		}
		return result.values.toList()
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		val title = doc.selectFirst("h1#fiction-title")?.text()?.trim()
			?: doc.selectFirst("meta[property=og:title]")?.attr("content")?.substringBefore('|')?.trim()
			?: manga.title
		val description = doc.selectFirst("meta[property=og:description]")?.attr("content")?.trim()
		val cover = doc.selectFirst("meta[property=og:image]")?.attr("content")
			?.let { if (it.startsWith("http")) it else it.toAbsoluteUrl(domain) }
		val author = doc.selectFirst("a[rel=author]")?.text()?.trim()?.takeIf { it.isNotBlank() }
		val chapters = doc.select("a[href^=/chapters/]")
			.mapIndexedNotNull { index, link ->
				val href = link.attr("href").toAbsoluteUrl(domain)
				val chapterTitle = link.text().trim().takeIf { it.isNotBlank() }
					?: return@mapIndexedNotNull null
				MangaChapter(
					id = generateUid(href),
					title = chapterTitle,
					number = (index + 1).toFloat(),
					volume = 0,
					url = href,
					uploadDate = 0L,
					source = source,
					scanlator = null,
					branch = null,
				)
			}
			.distinctBy { it.id }
		return manga.copy(
			title = title,
			altTitles = emptySet(),
			coverUrl = cover ?: manga.coverUrl,
			largeCoverUrl = cover ?: manga.largeCoverUrl,
			description = description,
			authors = setOfNotNull(author),
			state = MangaState.ONGOING,
			chapters = chapters,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		val content = doc.selectFirst("#user-content")
			?: doc.selectFirst("main")
			?: doc.body()
		content.select("script, style, iframe, ins, .ads, nav, header, footer").remove()
		val html = content.html().trim()
		if (html.isEmpty()) {
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

	private companion object {
		const val PAGE_SIZE = 20
		const val RATING_UNKNOWN = -1f
	}
}
