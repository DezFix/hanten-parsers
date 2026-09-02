package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAROCK", "MangaRock", "en")
internal class MangaRock(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAROCK, "mangarockteam.com") {
	override val datePattern = "MMMM dd, yyyy"
}
