package hanten.wre.app.parsers.site.ru.grouple

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.exception.AuthRequiredException
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.Manga
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.SortOrder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jsoup.HttpStatusException

@MangaSourceParser("USAGI", "Usagi", "ru")
internal class UsagiParser(
	context: MangaLoaderContext,
) : GroupleParser(context, MangaParserSource.USAGI, 1) {

	override val configKeyDomain = ConfigKey.Domain(*domains)

	// Usagi runs a stricter anti-bot filter than its sister sites ("Ошибка =)" 500 page,
	// "NOT FOUND" stubs, "логи записаны, скоро починим").
	// NOTE: do NOT override the user agent here: probing shows the site serves
	// the full pages to the shared Arora-based UA but answers common Chrome UAs
	// with stubs, so a "modern" UA makes things worse. Users can still override
	// it in source settings.
	override fun getRequestHeaders() = super.getRequestHeaders().newBuilder()
		.add("referer", "https://$domain/")
		.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.add("Upgrade-Insecure-Requests", "1")
		.add("Sec-Fetch-Dest", "document")
		.add("Sec-Fetch-Mode", "navigate")
		.add("Sec-Fetch-Site", "none")
		.add("Sec-Fetch-User", "?1")
		.build()

	// The site throttles parallel/rapid requests per IP ("NOT FOUND", tarpit, 500).
	// All document requests go through a single-flight mutex with gentle pacing,
	// so at most one request at a time ever hits the site from this process.
	private val requestMutex = Mutex()

	// Anti-bot stubs are often transient: retry document requests with backoff.
	// Auth and cancellation are never retried.
	override suspend fun getList(offset: Int, order: SortOrder, filter: MangaListFilter): List<Manga> =
		serialized { retryOnAntiBot { super.getList(offset, order, filter) } }

	override suspend fun getDetails(manga: Manga): Manga =
		serialized { retryOnAntiBot { super.getDetails(manga) } }

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> =
		serialized { retryOnAntiBot { super.getPages(chapter) } }

	override suspend fun getRelatedManga(seed: Manga): List<Manga> =
		serialized { super.getRelatedManga(seed) }

	private suspend fun <T> serialized(block: suspend () -> T): T = requestMutex.withLock {
		delay(PACING_MS)
		block()
	}

	private suspend fun <T> retryOnAntiBot(block: suspend () -> T): T {
		var lastError: Throwable? = null
		repeat(MAX_RETRIES) { attempt ->
			try {
				return block()
			} catch (e: AuthRequiredException) {
				throw e
			} catch (e: CancellationException) {
				throw e
			} catch (e: HttpStatusException) {
				// Retry only transient anti-bot failures (500 smiley page, rate limits).
				// Deterministic answers (404 stubs and everything else) fail fast
				// to avoid hammering and IP flagging.
				if (e.statusCode !in TRANSIENT_CODES || attempt == MAX_RETRIES - 1) {
					throw e
				}
				lastError = e
				delay(RETRY_DELAY_MS * (attempt + 1))
			}
		}
		throw lastError ?: ParseException("Usagi anti-bot protection", "https://$domain/")
	}

	companion object {

		// web.usagi.one filters aggressively (500 smiley / NOT FOUND stubs);
		// a.zazaza.me serves the same catalog without the wall, so it is primary.
		val domains = arrayOf("a.zazaza.me", "web.usagi.one")

		private const val MAX_RETRIES = 3
		private const val RETRY_DELAY_MS = 2000L
		private const val PACING_MS = 1000L
		private val TRANSIENT_CODES = intArrayOf(429, 500, 502, 503)
	}
}
