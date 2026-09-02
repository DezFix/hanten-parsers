package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGA_SCANTRAD", "MangaScantrad.io", "fr")
internal class MangaScantrad(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGA_SCANTRAD, "manga-scantrad.io") {
	override val datePattern = "d MMMM yyyy"
}
