package hanten.wre.app.parsers.site.comicaso.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.Manga
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.comicaso.ComicasoParser

@MangaSourceParser("COMICAZEN", "Comicazen", "id")
internal class Comicazen(context: MangaLoaderContext) :
	ComicasoParser(context, MangaParserSource.COMICAZEN, "comicazen.com", pageSize = 16) {

	override suspend fun getDetails(manga: Manga): Manga {
		val details = super.getDetails(manga)
		return details.copy(chapters = details.chapters?.reversed())
	}
}
