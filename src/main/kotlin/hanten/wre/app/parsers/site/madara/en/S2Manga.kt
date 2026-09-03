package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("S2MANGA", "S2Read", "en")
internal class S2Manga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.S2MANGA, "s2read.com") {
	override val datePattern = "MMMM dd, yyyy"
}
