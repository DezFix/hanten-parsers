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

	// Anti-bot stubs are often transient: retry document requests with backoff.
	// Auth and cancellation are never retried.
	override suspend fun getList(offset: Int, order: SortOrder, filter: MangaListFilter): List<Manga> =
		retryOnAntiBot { super.getList(offset, order, filter) }

	override suspend fun getDetails(manga: Manga): Manga =
		retryOnAntiBot { super.getDetails(manga) }

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> =
		retryOnAntiBot { super.getPages(chapter) }

	override suspend fun getRelatedManga(seed: Manga): List<Manga> =
		retryOnAntiBot { super.getRelatedManga(seed) }

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

		val domains = arrayOf("web.usagi.one")

		private const val MAX_RETRIES = 3
		private const val RETRY_DELAY_MS = 2000L
		private val TRANSIENT_CODES = intArrayOf(429, 500, 502, 503)
	}
}
