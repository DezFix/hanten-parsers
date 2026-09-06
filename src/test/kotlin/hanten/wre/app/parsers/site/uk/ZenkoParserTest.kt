package hanten.wre.app.parsers.site.uk

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.SortOrder

internal class ZenkoParserTest {

	private val parser = MangaLoaderContextMock.newParserInstance(MangaParserSource.ZENKO)

	@Test
	fun listDetailsPages() = runTest {
		val list = parser.getList(0, SortOrder.UPDATED, MangaListFilter())
		assertTrue(list.isNotEmpty(), "list is empty")
		val details = parser.getDetails(list.first())
		assertTrue(!details.chapters.isNullOrEmpty(), "no chapters")
		val pages = parser.getPages(details.chapters!!.first())
		assertTrue(pages.isNotEmpty(), "no pages")
		assertTrue(pages.all { it.url.startsWith("http") }, "bad page urls")
	}

	@Test
	fun search() = runTest {
		val list = parser.getList(0, SortOrder.UPDATED, MangaListFilter(query = "варвар"))
		assertTrue(list.isNotEmpty(), "search is empty")
	}
}
