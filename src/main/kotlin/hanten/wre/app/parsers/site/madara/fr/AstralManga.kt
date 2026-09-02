package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Website, come back soon")
@MangaSourceParser("ASTRALMANGA", "AstralManga", "fr")
internal class AstralManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ASTRALMANGA, "astral-manga.fr") {
	override val datePattern = "dd/MM/yyyy"
}
