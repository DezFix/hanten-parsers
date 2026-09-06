package hanten.wre.app.parsers.site.ru

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
import java.text.SimpleDateFormat
import java.util.*

// The site sits behind a Vercel Security Checkpoint ("We're verifying your
// browser" 429 page) that rejects plain HTTP clients with 429 Too Many
// Requests. All page fetches go through WebView (context.evaluateJs), which
// executes the checkpoint's JavaScript and then returns the rendered DOM.
@MangaSourceParser("JOILMANG", "JoiMang", "ru")
internal class JoilmangParser(
	context: MangaLoaderContext,
) : PagedMangaParser(context, MangaParserSource.JOILMANG, 24) {

	override val configKeyDomain = ConfigKey.Domain("joilmang.com")

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions()

	private suspend fun fetchDocument(url: String): Document {
		val script = """
			(() => {
				const checkpoint = (document.title || '').toLowerCase().includes('verifying your browser') ||
					(document.body && document.body.innerText.includes('Vercel Security Checkpoint'));
				if (checkpoint) {
					return 'VERCEL_CHECKPOINT';
				}
				const ready = document.body && document.querySelector(
					'a.jm-card-hover, main, header, h1.section-heading, details ol, picture[data-reader-page]'
				);
				if (ready) {
					window.stop();
					const elementsToRemove = document.querySelectorAll('script, iframe, object, embed, style');
					elementsToRemove.forEach(el => el.remove());
					return document.documentElement.outerHTML;
				}
				return null;
			})();
		""".trimIndent()
		val rawHtml = context.evaluateJs(url, script, 30000L)
			?: throw ParseException("Failed to load page", url)
		if (rawHtml == "VERCEL_CHECKPOINT") {
			throw ParseException("Vercel checkpoint was not resolved", url)
		}
		val html = if (rawHtml.startsWith("\"") && rawHtml.endsWith("\"")) {
			rawHtml.substring(1, rawHtml.length - 1)
				.replace("\\\"", "\"")
				.replace("\\n", "\n")
				.replace("\\r", "\r")
				.replace("\\t", "\t")
				.replace(Regex("""\\u([0-9A-Fa-f]{4})""")) { match ->
					match.groupValues[1].toInt(16).toChar().toString()
				}
		} else rawHtml
		return Jsoup.parse(html, url)
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val url = buildString {
			append("https://")
			append(domain)
			append("/catalog")
			val params = ArrayList<String>(2)
			if (page > 1) {
				params.add("page=$page")
			}
			filter.query?.takeIf { it.isNotEmpty() }?.let {
				params.add("q=${it.urlEncoded()}")
			}
			if (params.isNotEmpty()) {
				append('?')
				append(params.joinToString("&"))
			}
		}
		val doc = fetchDocument(url)
		val cards = doc.select("a.jm-card-hover[href]").mapNotNull(::parseCard)
		if (cards.isEmpty()) {
			doc.parseFailed("No manga items found")
		}
		return cards
	}

	private fun parseCard(a: Element): Manga? {
		val href = a.attrAsRelativeUrl("href")
		if (!href.startsWith("/manga/")) {
			return null
		}
		val img = a.selectFirst("img[src]") ?: return null
		val title = img.attr("alt").trim().takeUnless { it.isEmpty() }
			?: a.text().trim().takeUnless { it.isEmpty() }
			?: return null
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
			coverUrl = img.attrAsAbsoluteUrlOrNull("src"),
			contentRating = null,
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = fetchDocument(manga.url.toAbsoluteUrl(domain))
		val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
		val title = doc.selectFirst("h1.section-heading")?.text()?.trim()?.takeUnless { it.isEmpty() }
			?: manga.title
		val description = doc.selectFirst("meta[property=og:description]")?.attr("content")?.trim()
		val cover = doc.selectFirst("meta[property=og:image]")?.attrAsAbsoluteUrlOrNull("content")
			?: manga.coverUrl
		val meta = doc.select("dl div").associate { div ->
			val label = div.selectFirst("dt")?.text()?.trim().orEmpty()
			label to div.selectFirst("dd")
		}
		val authors = buildSet {
			meta.entries.firstOrNull { it.key.contains("автор", ignoreCase = true) }
				?.value?.select("a")?.eachText()?.let(::addAll)
			meta.entries.firstOrNull { it.key.contains("художник", ignoreCase = true) }
				?.value?.select("a")?.eachText()?.let(::addAll)
		}
		val statusText = meta.entries.firstOrNull { it.key.contains("статус", ignoreCase = true) }
			?.value?.text().orEmpty()
		val state = when {
			statusText.contains("продолж", ignoreCase = true) ||
				statusText.contains("онгоинг", ignoreCase = true) ||
				statusText.contains("ongoing", ignoreCase = true) -> MangaState.ONGOING

			statusText.contains("заверш", ignoreCase = true) ||
				statusText.contains("completed", ignoreCase = true) -> MangaState.FINISHED

			statusText.contains("анонс", ignoreCase = true) -> MangaState.UPCOMING
			statusText.contains("замороз", ignoreCase = true) ||
				statusText.contains("пауза", ignoreCase = true) -> MangaState.PAUSED

			else -> null
		}
		val tags = doc.select("dl a[href^=/genre/], dl a[href^=/tag/]").mapNotNullToSet { a ->
			val title = a.text().trim().toTitleCase(sourceLocale).takeIf { it.isNotEmpty() }
				?: return@mapNotNullToSet null
			MangaTag(
				title = title,
				key = a.attr("href").removeSuffix("/").substringAfterLast("/").urlDecode(),
				source = source,
			)
		}
		val chapters = doc.select("details ol li a[href]").mapNotNull { a ->
			val href = a.attrAsRelativeUrl("href")
			val segments = href.removeSuffix("/").split("/")
			if (segments.size < 4) {
				return@mapNotNull null
			}
			val volume = segments[segments.size - 2].toIntOrNull() ?: 0
			val number = a.selectFirst("b")?.text()?.trim()?.toFloatOrNull()
				?: segments.last().toFloatOrNull() ?: 0f
			val dateText = a.selectFirst("span")?.text()?.trim()
			MangaChapter(
				id = generateUid(href),
				url = href,
				title = null,
				number = number,
				volume = volume,
				uploadDate = dateFormat.parseSafe(dateText),
				scanlator = null,
				branch = null,
				source = source,
			)
		}.reversed()
		if (chapters.isEmpty()) {
			throw ParseException("Chapter list not found", manga.url)
		}
		return manga.copy(
			title = title,
			authors = authors,
			description = description,
			coverUrl = cover,
			state = state,
			tags = tags,
			chapters = chapters,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = fetchDocument(chapter.url.toAbsoluteUrl(domain))
		return doc.select("picture[data-reader-page] img[src]").mapNotNull { img ->
			val url = img.attrAsAbsoluteUrlOrNull("src") ?: return@mapNotNull null
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}.ifEmpty { throw ParseException("No pages found", chapter.url) }
	}
}
