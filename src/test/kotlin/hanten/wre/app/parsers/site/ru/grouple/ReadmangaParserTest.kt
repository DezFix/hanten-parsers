package hanten.wre.app.parsers.site.ru.grouple

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaState
import hanten.wre.app.parsers.model.RATING_UNKNOWN
import hanten.wre.app.test_util.mangaOf
import org.junit.jupiter.api.Assertions.assertEquals

internal class ReadmangaParserTest {

	private val parser = MangaLoaderContextMock.newParserInstance(MangaParserSource.READMANGA_RU)

	@Test
	fun descriptionIsPresent() = runTest {
		val details = parser.getDetails(mangaOf(MangaParserSource.READMANGA_RU, "/naruto"))
		assertTrue(!details.description.isNullOrEmpty(), "no description")
		assertTrue(!details.chapters.isNullOrEmpty(), "no chapters")
		assertTrue(details.rating != RATING_UNKNOWN, "no rating")
		assertEquals(MangaState.FINISHED, details.state)
	}
}
