package hanten.wre.app.parsers.site.revision

import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.search.MangaSearchQuery
import kotlin.time.Duration.Companion.minutes

/**
 * Revision sweep for RU/UA sources: list + details + pages + domain.
 * Run: ./gradlew test --tests "*RuUaRevisionTest*"
 */
internal class RuUaRevisionTest {

	private val context = MangaLoaderContextMock
	private val timeout = 3.minutes

	@ParameterizedTest(name = "{index}|rev-list|{0}")
	@EnumSource(MangaParserSource::class, mode = INCLUDE, names = [
		"READMANGA_RU", "MINTMANGA", "SELFMANGA",
		"MANGACHAN", "HENCHAN", "YAOICHAN",
		"REMANGA", "MANGALIB", "MANGALIB_COM",
		"SENKURO", "TOMILOLIB", "WAMANGA", "ZENMANGA",
		"COMX", "DESUME", "ACOMICS", "MANGABUFF", "NUDEMOON",
		"RECOMICS", "JOILMANG", "MANGA_WTF",
		"DGMANGA", "HENTAIUKR", "HONEYMANGA", "MANGAINUA", "ZENKO",
		// USAGI and HENTAILIB are @Broken (hidden in the app): kept out of the sweep on purpose
		"ALLHENTAI", "YAOILIB",
	])
	fun list(source: MangaParserSource) = runTest(timeout = timeout) {
		val parser = context.newParserInstance(source)
		val list = parser.getList(MangaSearchQuery.EMPTY)
		assertTrue(list.isNotEmpty(), "list is empty for $source")
		assertTrue(list.all { it.source == source })
	}

	@ParameterizedTest(name = "{index}|rev-details|{0}")
	@EnumSource(MangaParserSource::class, mode = INCLUDE, names = [
		"READMANGA_RU", "MINTMANGA", "SELFMANGA",
		"MANGACHAN", "HENCHAN", "YAOICHAN",
		"REMANGA", "MANGALIB", "MANGALIB_COM",
		"SENKURO", "TOMILOLIB", "WAMANGA", "ZENMANGA",
		"COMX", "DESUME", "ACOMICS", "MANGABUFF", "NUDEMOON",
		"RECOMICS", "JOILMANG", "MANGA_WTF",
		"DGMANGA", "HENTAIUKR", "HONEYMANGA", "MANGAINUA", "ZENKO",
		// USAGI and HENTAILIB are @Broken (hidden in the app): kept out of the sweep on purpose
		"ALLHENTAI", "YAOILIB",
	])
	fun details(source: MangaParserSource) = runTest(timeout = timeout) {
		val parser = context.newParserInstance(source)
		val list = parser.getList(MangaSearchQuery.EMPTY)
		assertTrue(list.isNotEmpty(), "list is empty for $source")
		val manga = list.first()
		val details = parser.getDetails(manga)
		assertTrue(!details.chapters.isNullOrEmpty(), "no chapters for ${manga.publicUrl}")
	}

	@ParameterizedTest(name = "{index}|rev-pages|{0}")
	@EnumSource(MangaParserSource::class, mode = INCLUDE, names = [
		"READMANGA_RU", "MINTMANGA", "SELFMANGA",
		"MANGACHAN", "HENCHAN", "YAOICHAN",
		"REMANGA", "MANGALIB", "MANGALIB_COM",
		"SENKURO", "TOMILOLIB", "WAMANGA", "ZENMANGA",
		"COMX", "DESUME", "ACOMICS", "MANGABUFF", "NUDEMOON",
		"RECOMICS", "JOILMANG", "MANGA_WTF",
		"DGMANGA", "HENTAIUKR", "HONEYMANGA", "MANGAINUA", "ZENKO",
		// USAGI and HENTAILIB are @Broken (hidden in the app): kept out of the sweep on purpose
		"ALLHENTAI", "YAOILIB",
	])
	fun pages(source: MangaParserSource) = runTest(timeout = timeout) {
		val parser = context.newParserInstance(source)
		val list = parser.getList(MangaSearchQuery.EMPTY)
		assertTrue(list.isNotEmpty(), "list is empty for $source")
		val manga = list.first()
		val chapter = parser.getDetails(manga).chapters?.firstOrNull()
			?: error("no chapter at ${manga.publicUrl}")
		val pages = parser.getPages(chapter)
		assertTrue(pages.isNotEmpty(), "no pages for chapter ${chapter.url}")
		val pageUrl = parser.getPageUrl(pages.first())
		assertTrue(pageUrl.isNotEmpty(), "empty page url")
		assertTrue(pageUrl.startsWith("http"), "page url is not absolute: $pageUrl")
	}

	@ParameterizedTest(name = "{index}|rev-domain|{0}")
	@EnumSource(MangaParserSource::class, mode = INCLUDE, names = [
		"READMANGA_RU", "MINTMANGA", "SELFMANGA",
		"MANGACHAN", "HENCHAN", "YAOICHAN",
		"REMANGA", "MANGALIB", "MANGALIB_COM",
		"SENKURO", "TOMILOLIB", "WAMANGA", "ZENMANGA",
		"COMX", "DESUME", "ACOMICS", "MANGABUFF", "NUDEMOON",
		"RECOMICS", "JOILMANG", "MANGA_WTF",
		"DGMANGA", "HENTAIUKR", "HONEYMANGA", "MANGAINUA", "ZENKO",
		// USAGI and HENTAILIB are @Broken (hidden in the app): kept out of the sweep on purpose
		"ALLHENTAI", "YAOILIB",
	])
	fun domain(source: MangaParserSource) = runTest(timeout = timeout) {
		val parser = context.newParserInstance(source)
		val url = HttpUrl.Builder().host(parser.domain).scheme("https").toString()
		val response = context.doRequest(url, source)
		val realHost = response.request.url.host
		assertTrue(
			parser.domain == realHost || realHost.endsWith("." + parser.domain),
			"domain ${parser.domain} redirects to $realHost",
		)
	}
}
