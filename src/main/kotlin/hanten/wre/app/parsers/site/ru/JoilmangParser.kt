package hanten.wre.app.parsers.site.ru

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ContentUnavailableException
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

	private suspend fun fetchDocument(url: String, readySelector: String): Document {
		// Plain HTTP first: the Vercel checkpoint is selective, not permanent.
		// WebView (slow, ~seconds) is only a fallback for the checkpoint page.
		return fetchDocumentOrPlain(url, readySelector) ?: fetchDocumentViaWebView(url, readySelector)
	}

	private suspend fun fetchDocumentOrPlain(url: String, readySelector: String?): Document? {
		val doc = runCatching { webClient.httpGet(url).parseHtml() }.getOrNull() ?: return null
		if (isCheckpoint(doc)) {
			return null
		}
		if (readySelector != null && doc.selectFirst(readySelector) == null) {
			return null
		}
		return doc
	}

	/**
	 * Takedown notice ("removed over a copyright holder complaint") lives in the
	 * page content; the footer mentions правообладателям on EVERY page, so it is
	 * stripped first to avoid false positives on catalogs and healthy titles.
	 */
	private fun isTakedown(doc: Document): Boolean {
		val body = doc.body()?.clone() ?: return false
		body.select("footer, header, nav").remove()
		val text = body.text()
		return text.contains("по требованию правообладателя", ignoreCase = true) ||
			text.contains("copyright holder", ignoreCase = true) ||
			text.contains("dmca", ignoreCase = true) ||
			(text.contains("правообладател", ignoreCase = true) &&
				(text.contains("удален", ignoreCase = true) || text.contains("заблокирован", ignoreCase = true)))
	}

	private fun isCheckpoint(doc: Document): Boolean {
		if ((doc.title() ?: "").contains("verifying your browser", ignoreCase = true)) {
			return true
		}
		return doc.body()?.text()?.contains("Vercel Security Checkpoint") == true
	}

	private suspend fun fetchDocumentViaWebView(url: String, readySelector: String): Document {
		// NOTE: the ready check must require the actual content (e.g. chapter links),
		// not just a container: the site renders chapter lists asynchronously and an
		// empty <ol> would otherwise be accepted too early.
		val script = """
			(() => {
				const checkpoint = (document.title || '').toLowerCase().includes('verifying your browser') ||
					(document.body && document.body.innerText.includes('Vercel Security Checkpoint'));
				if (checkpoint) {
					return 'VERCEL_CHECKPOINT';
				}
				const ready = document.body && document.querySelector('$readySelector');
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
		val doc = fetchDocument(url, "a.jm-card-hover")
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
		// Fast path for takedown stubs: plain HTML already carries the notice
		// ("Главы скрыты по требованию правообладателя") while chapters never
		// render — no need to wait out the WebView fallback.
		val plain = fetchDocumentOrPlain(manga.url.toAbsoluteUrl(domain), null)
		if (plain != null && plain.select("details ol li a[href]").isEmpty() && isTakedown(plain)) {
			return parseTakedownDetails(manga, plain)
		}
		// Chapters are JS-rendered: plain HTML only qualifies if it already has them.
		val doc = fetchDocument(manga.url.toAbsoluteUrl(domain), "details ol li a[href]")
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
			// Takedown reached via WebView path (e.g. after a checkpoint):
			// mark unavailable instead of a cryptic parse error.
			if (isTakedown(doc)) {
				return manga.copy(
					state = MangaState.RESTRICTED,
					chapters = emptyList(),
				)
			}
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

	private fun parseTakedownDetails(manga: Manga, doc: Document): Manga {
		val title = doc.selectFirst("h1.section-heading")?.text()?.trim()?.takeUnless { it.isEmpty() }
			?: doc.selectFirst("meta[property=og:title]")?.attr("content")?.trim()?.takeUnless { it.isEmpty() }
			?: manga.title
		return manga.copy(
			title = title,
			description = doc.selectFirst("meta[property=og:description]")?.attr("content")?.trim(),
			coverUrl = doc.selectFirst("meta[property=og:image]")?.attrAsAbsoluteUrlOrNull("content")
				?: manga.coverUrl,
			state = MangaState.RESTRICTED,
			chapters = emptyList(),
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = fetchDocument(chapter.url.toAbsoluteUrl(domain), "picture[data-reader-page]")
		return doc.select("picture[data-reader-page] img[src]").mapNotNull { img ->
			val url = img.attrAsAbsoluteUrlOrNull("src") ?: return@mapNotNull null
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}.ifEmpty {
			if (isTakedown(doc)) {
				throw ContentUnavailableException("Тайтл удалён по требованию правообладателя")
			}
			throw ParseException("No pages found", chapter.url)
		}
	}
}
