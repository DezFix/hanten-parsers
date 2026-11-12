package hanten.wre.app.parsers.site.novel.ru

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URLEncoder
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
 * Ranobe.me is a Russian online library with a plain HTML catalog, book pages
 * and chapter pages. Text is rendered from `div.ReadContent`.
 */
@MangaSourceParser("RANOBE_ME", "Ranobe.me", "ru", type = ContentType.NOVEL)
internal class RanobeMeParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.RANOBE_ME, PAGE_SIZE) {

	override val configKeyDomain = ConfigKey.Domain("ranobe.me")

	override val availableSortOrders: Set<SortOrder> = setOf(
		SortOrder.POPULARITY,
		SortOrder.UPDATED,
		SortOrder.NEWEST,
		SortOrder.ALPHABETICAL,
		SortOrder.RELEVANCE,
	)

	override val defaultSortOrder: SortOrder = SortOrder.UPDATED

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isMultipleTagsSupported = false,
			isSearchSupported = true,
			isSearchWithFiltersSupported = false,
		)

	override suspend fun getFilterOptions(): MangaListFilterOptions {
		val doc = webClient.httpGet("https://$domain/catalog", getRequestHeaders()).parseHtml()
		val tags = LinkedHashMap<String, MangaTag>()
		for (link in doc.select("a[href^=/catalog/genre/]")) {
			val key = link.attr("href").substringAfterLast('/').trim()
			val title = link.text().trim()
			if (key.isEmpty() || title.isEmpty()) continue
			tags.putIfAbsent(key, MangaTag(title = title, key = key, source = source))
		}
		return MangaListFilterOptions(availableTags = tags.values.toSet())
	}

	override suspend fun getListPage(
		page: Int,
		order: SortOrder,
		filter: MangaListFilter,
	): List<Manga> {
		val query = filter.query?.trim().orEmpty()
		val genre = filter.tags.firstOrNull()?.key
		val url = when {
			query.isNotEmpty() -> {
				val encoded = URLEncoder.encode(query, "UTF-8")
				"https://$domain/index.php?section=search&str=$encoded&page=$page"
			}

			genre != null -> "https://$domain/catalog/genre/$genre?page=$page"
			else -> {
				val sort = when (order) {
					SortOrder.POPULARITY -> "stat"
					SortOrder.ALPHABETICAL -> "title"
					else -> null
				}
				if (sort != null) {
					"https://$domain/catalog?page=$page&sort=$sort"
				} else {
					"https://$domain/catalog?page=$page"
				}
			}
		}
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val result = LinkedHashMap<Long, Manga>()
		for (card in doc.select(".FicTable")) {
			val manga = parseCard(card) ?: continue
			result.putIfAbsent(manga.id, manga)
		}
		return result.values.toList()
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		val head = doc.selectFirst("div.FicHead") ?: doc
		val title = head.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotBlank() } ?: manga.title
		val coverElement = doc.selectFirst(".FicCover img")
		val cover = coverElement?.let { img ->
			img.absUrl("data-src").ifBlank { img.absUrl("src") }
		}?.takeIf { it.isNotBlank() }
		val altTitle = head.selectFirst(".FicHeadRight .tr .content")?.childNode(0)?.toString()?.trim()
			?.takeIf { it.isNotBlank() && it != title }
		val description = doc.selectFirst("[id^=summary_]")?.text()?.trim()?.takeIf { it.isNotBlank() }
		val tags = head.select("a[href^=/catalog/genre/]").mapNotNull { link ->
			val key = link.attr("href").substringAfterLast('/').trim()
			val name = link.text().trim()
			if (key.isEmpty() || name.isEmpty()) null else MangaTag(name, key, source)
		}.toSet()
		val authors = head.select("a[href^=/catalog/author/]").mapNotNull { link ->
			link.text().trim().takeIf { it.isNotBlank() }
		}.toSet()
		return manga.copy(
			title = title,
			altTitles = setOfNotNull(altTitle),
			coverUrl = cover ?: manga.coverUrl,
			largeCoverUrl = cover ?: manga.largeCoverUrl,
			description = description ?: manga.description,
			tags = tags,
			authors = authors,
			chapters = parseChapters(doc),
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		val content = doc.selectFirst("div.ReadContent div.chapter")
			?: doc.selectFirst("div.ReadContent")
			?: return emptyList()
		content.select("script, style, iframe, ins, .ads, .FullWidth, .read_nav_button").remove()
		for (image in content.select("img")) {
			val src = image.absUrl("data-src").ifBlank { image.absUrl("src") }
			if (src.isBlank()) {
				image.remove()
				continue
			}
			image.attr("src", src)
			image.removeAttr("data-src")
		}
		content.select("a[name]").forEach { it.removeAttr("name") }
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

	private fun parseCard(card: Element): Manga? {
		val link = card.selectFirst(".FicTable_Title a[href^=/ranobe]")
			?: card.selectFirst("a[href^=/ranobe]")
			?: return null
		val href = link.absUrl("href").ifBlank { "https://$domain${link.attr("href")}" }
		val title = link.text().trim()
		if (title.isEmpty()) return null
		val cover = card.selectFirst(".FicTable_Cover img")?.absUrl("src")?.takeIf { it.isNotBlank() }
		return Manga(
			id = generateUid(href),
			title = title,
			altTitles = emptySet(),
			url = href,
			publicUrl = href,
			rating = RATING_UNKNOWN,
			contentRating = null,
			coverUrl = cover,
			tags = emptySet(),
			state = parseState(card),
			authors = emptySet(),
			largeCoverUrl = cover,
			source = source,
		)
	}

	private fun parseChapters(doc: Document): List<MangaChapter> {
		val result = ArrayList<MangaChapter>()
		val seen = HashSet<String>()
		for (link in doc.select(".FicContentsChapterName a[href^=/ranobe]")) {
			val href = link.absUrl("href").ifBlank { "https://$domain${link.attr("href")}" }
			if (!seen.add(href)) continue
			val title = link.text().trim().takeIf { it.isNotBlank() } ?: continue
			result += MangaChapter(
				id = generateUid(href),
				title = title,
				number = href.substringAfterLast('/').toFloatOrNull()?.plus(1f) ?: (result.size + 1).toFloat(),
				volume = 0,
				url = href,
				scanlator = null,
				uploadDate = 0L,
				branch = null,
				source = source,
			)
		}
		return result
	}

	private fun parseState(card: Element): MangaState? = when {
		card.selectFirst(".RanobeCompleted") != null -> MangaState.FINISHED
		card.selectFirst(".RanobeProcess, .RanobeOngoing") != null -> MangaState.ONGOING
		card.selectFirst(".RanobeFrozen, .RanobePaused") != null -> MangaState.PAUSED
		card.selectFirst(".RanobeAbandoned, .RanobeDropped") != null -> MangaState.ABANDONED
		else -> null
	}

	private companion object {
		const val PAGE_SIZE = 20
		const val RATING_UNKNOWN = -1f
	}
}
