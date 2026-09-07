package hanten.wre.app.parsers.site.ru

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.SortOrder

internal class WaMangaParserTest {

	private val parser = MangaLoaderContextMock.newParserInstance(MangaParserSource.WAMANGA)

	@Test
	fun listDetailsPages() = runTest {
		val list = parser.getList(0, SortOrder.UPDATED, MangaListFilter())
		assertTrue(list.isNotEmpty(), "list is empty")
		val details = parser.getDetails(list.first())
		assertTrue(!details.chapters.isNullOrEmpty(), "no chapters")
		val chapter = details.chapters!!.firstOrNull { it.number > 0 } ?: details.chapters!!.first()
		val pages = parser.getPages(chapter)
		assertTrue(pages.isNotEmpty(), "no pages")
	}
}
