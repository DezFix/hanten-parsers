package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TOPREADMANHWA", "TopReadManhwa", "en")
internal class TopReadManhwa(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TOPREADMANHWA, "topreadmanhwa.com") {
	override val datePattern = "MM/dd/yyyy"
}
