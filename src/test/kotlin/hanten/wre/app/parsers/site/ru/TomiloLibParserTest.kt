package hanten.wre.app.parsers.site.ru

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.SortOrder
import hanten.wre.app.test_util.mangaOf

internal class TomiloLibParserTest {

	private val parser = MangaLoaderContextMock.newParserInstance(MangaParserSource.TOMILOLIB)

	@Test
	fun detailsLoadsFullChapterList() = runTest {
		val manga = mangaOf(
			MangaParserSource.TOMILOLIB,
			"/titles/ya-prokachivayus'-vo-sne-ubivaya-monstrov",
		)
		val details = parser.getDetails(manga)
		assertTrue(!details.chapters.isNullOrEmpty(), "no chapters")
		assertTrue((details.chapters?.size ?: 0) >= 300, "chapter list is truncated: " + details.chapters?.size)
		val pages = parser.getPages(details.chapters!!.first())
		assertTrue(pages.isNotEmpty(), "no pages")
	}

	@Test
	fun search() = runTest {
		val list = parser.getList(0, SortOrder.UPDATED, MangaListFilter(query = "прокачиваюсь"))
		assertTrue(list.isNotEmpty(), "search is empty")
	}
}
