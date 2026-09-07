package hanten.wre.app.parsers.site.ru

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import hanten.wre.app.parsers.MangaLoaderContextMock
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.test_util.mangaOf

internal class SenkuroParserTest {

	private val parser = MangaLoaderContextMock.newParserInstance(MangaParserSource.SENKURO)

	@Test
	fun detailsHasRating() = runTest {
		val manga = mangaOf(
			MangaParserSource.SENKURO,
			"TUFOR0E6MTU1Mjk0NzY1MjYzMzczODcz,,the-heavenly-demon-wants-a-quiet-life",
		)
		val details = parser.getDetails(manga)
		assertEquals(0.896f, details.rating, 0.001f)
	}
}
