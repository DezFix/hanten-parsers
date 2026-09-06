package hanten.wre.app.parsers.site.ru

import okhttp3.Headers
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("MANGALIB_COM", "MangaLib.com", "ru")
internal class MangaLibComParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.MANGALIB_COM, 24) {

	override val configKeyDomain = ConfigKey.Domain("manga-lib.com")

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	override fun getRequestHeaders(): Headers = super.getRequestHeaders().newBuilder()
		.set("Referer", "https://$domain/manga/")
		.build()

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val query = filter.query?.takeIf { it.isNotEmpty() }
		val url = if (query != null) {
			buildString {
				append("https://")
				append(domain)
				append("/index.php?do=search&subaction=search&story=")
				append(query.urlEncoded())
				if (page > 1) {
					append("&search_start=")
					append((page - 1) * pageSize + 1)
				}
			}
		} else if (page > 1) {
			"https://$domain/manga/page/$page/"
		} else {
			"https://$domain/manga/"
		}
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val results = doc.select("article.short-tablet").mapNotNull(::parseSearchResult)
		if (results.isNotEmpty()) {
			return results
		}
		if (query != null && doc.getElementsContainingOwnText("найдено 0").isNotEmpty()) {
			return emptyList()
		}
		doc.parseFailed("No manga items found")
	}

	private fun parseSearchResult(article: Element): Manga? {
		val link = article.select("div.st-title a[href]").lastOrNull { it.attr("href").endsWith(".html") }
			?: return null
		val title = link.text().trim().cleanTitle().takeUnless { it.isEmpty() } ?: return null
		return Manga(
			id = generateUid(link.attrAsRelativeUrl("href")),
			url = link.attrAsRelativeUrl("href"),
			publicUrl = link.attrAsAbsoluteUrl("href"),
			title = title,
			altTitles = emptySet(),
			authors = article.select("ul.sh-list a[href*=xfsearch/author/]").eachText().toSet(),
			description = article.selectFirst(".st-desc")?.text()?.trim(),
			tags = emptySet(),
			rating = RATING_UNKNOWN,
			state = null,
			coverUrl = article.selectFirst("a.st-poster img")?.attrAsAbsoluteUrlOrNull("src"),
			contentRating = null,
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
		val info = doc.select("ul.mangainfo li").associate { li ->
			val label = li.children().firstOrNull { it.tagName() == "span" }
				?.text()?.removeSuffix(":")?.trim().orEmpty()
			label to li
		}
		val stateText = info["Перевод"]?.text().orEmpty()
		val state = when {
			stateText.contains("продолжает", ignoreCase = true) -> MangaState.ONGOING
			stateText.contains("заверш", ignoreCase = true) -> MangaState.FINISHED
			else -> null
		}
		val scanlator = info["Переводчик(и)"]?.select("a")?.eachText()?.joinToString()
			?.takeUnless { it.isEmpty() }
		val chapters = doc.select("ul.chapters-list a.item-serial[href]").mapNotNull { a ->
			val href = a.attrAsRelativeUrl("href")
			val titleBlock = a.selectFirst(".item-title") ?: return@mapNotNull null
			val titleText = titleBlock.text()
			val number = chapterNumberRegex.find(titleText)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
			val volume = volumeRegex.find(titleText)?.groupValues?.get(1)?.toIntOrNull() ?: 0
			val dateText = chapterDateRegex.find(titleText)?.groupValues?.get(1)
			MangaChapter(
				id = generateUid(href),
				url = href,
				title = "Том $volume. Глава ${number.toChapterString()}".takeIf { volume > 0 }
					?: "Глава ${number.toChapterString()}",
				number = number,
				volume = volume,
				uploadDate = dateFormat.parseSafe(dateText),
				scanlator = scanlator,
				branch = null,
				source = source,
			)
		}.reversed()
		return manga.copy(
			title = doc.selectFirst("#fheader h1")?.text()?.trim()?.cleanTitle()
				?.takeUnless { it.isEmpty() } ?: manga.title,
			authors = info["Автор"]?.select("a")?.eachText()?.toSet().orEmpty(),
			description = doc.selectFirst("#fdesc")?.text()?.trim(),
			coverUrl = doc.selectFirst(".fposter img")?.attrAsAbsoluteUrlOrNull("src") ?: manga.coverUrl,
			state = state,
			tags = doc.select("ul.mangainfo a[href*=/tags/]").mapNotNullToSet { a ->
				val title = a.text().trim().toTitleCase(sourceLocale).takeIf { it.isNotEmpty() }
					?: return@mapNotNullToSet null
				MangaTag(
					title = title,
					key = a.attr("href").removeSuffix("/").substringAfterLast("/").urlDecode(),
					source = source,
				)
			},
			chapters = chapters,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain), getRequestHeaders()).parseHtml()
		return doc.select("a.mangaPage[data-i]").mapNotNull { a ->
			val url = a.attrAsAbsoluteUrlOrNull("data-i") ?: return@mapNotNull null
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}.ifEmpty { throw ParseException("No pages found", chapter.url) }
	}

	private fun String.cleanTitle(): String {
		return removePrefix("Читать ").removeSuffix(" онлайн.").trim()
	}

	private fun Float.toChapterString(): String {
		return if (this == kotlin.math.floor(this)) {
			toInt().toString()
		} else {
			toString()
		}
	}

	private companion object {

		val chapterNumberRegex = Regex("Глава\\s+([\\d.]+)")
		val volumeRegex = Regex("Том\\s+(\\d+)")
		val chapterDateRegex = Regex("(\\d{2}\\.\\d{2}\\.\\d{4})")
	}
}
