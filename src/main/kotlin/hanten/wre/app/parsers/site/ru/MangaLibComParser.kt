package hanten.wre.app.parsers.site.ru

import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
			return filterDeadCovers(results)
		}
		if (query != null && doc.getElementsContainingOwnText("найдено 0").isNotEmpty()) {
			return emptyList()
		}
		doc.parseFailed("No manga items found")
	}

	private suspend fun filterDeadCovers(list: List<Manga>): List<Manga> = coroutineScope {
		list.map { manga ->
			async(Dispatchers.IO) {
				val cover = manga.coverUrl
				if (cover.isNullOrEmpty() || isCoverAlive(cover)) manga else null
			}
		}.awaitAll().filterNotNull()
	}

	private suspend fun isCoverAlive(url: String): Boolean = runCatching {
		webClient.httpHead(url.toHttpUrl()).use { it.code == 200 }
	}.getOrDefault(false)

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
		val url = manga.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val result = parseDetails(manga, doc)
		if (!result.chapters.isNullOrEmpty()) {
			return result
		}
		// The chapter list is occasionally rendered empty (server-side flake):
		// refetch once before falling back to WebView.
		val retry = parseDetails(manga, webClient.httpGet(url, getRequestHeaders()).parseHtml())
		if (!retry.chapters.isNullOrEmpty()) {
			return retry
		}
		// Chapters are JS-rendered: fall back to WebView (a real browser in production,
		// unavailable in JVM tests — plain HTTP stays the fast path).
		val rendered = fetchRendered(url, "ul.chapters-list a.item-serial") ?: return retry
		return parseDetails(manga, rendered)
	}

	private fun parseDetails(manga: Manga, doc: Document): Manga {
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
		val url = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(url, getRequestHeaders()).parseHtml()
		val pages = parsePages(doc)
		if (pages.isNotEmpty()) {
			return pages
		}
		val rendered = fetchRendered(url, "a.mangaPage[data-i]")
			?: throw ParseException("No pages found", chapter.url)
		return parsePages(rendered).ifEmpty { throw ParseException("No pages found", chapter.url) }
	}

	private fun parsePages(doc: Document): List<MangaPage> {
		return doc.select("a.mangaPage[data-i]").mapNotNull { a ->
			val url = a.attrAsAbsoluteUrlOrNull("data-i") ?: return@mapNotNull null
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}

	private suspend fun fetchRendered(url: String, selector: String): Document? {
		val script = """
			(() => {
				const ready = document.body && document.querySelector('$selector');
				if (ready) {
					window.stop();
					return document.documentElement.outerHTML;
				}
				return null;
			})();
		""".trimIndent()
		val rawHtml = runCatching { context.evaluateJs(url, script, 30000L) }.getOrNull()
			?: return null
		val html = rawHtml.unquoteJs()
		return Jsoup.parse(html, url)
	}

	private fun String.unquoteJs(): String {
		var s = this
		if (s.length >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
			s = s.substring(1, s.length - 1)
				.replace("\\\"", "\"")
				.replace("\\n", "\n")
				.replace("\\r", "\r")
				.replace("\\t", "\t")
				.replace(Regex("""\\u([0-9A-Fa-f]{4})""")) { match ->
					match.groupValues[1].toInt(16).toChar().toString()
				}
		}
		return s
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
