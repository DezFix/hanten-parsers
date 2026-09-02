package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ARCTICSCAN", "ArcticScan", "pt")
internal class ArcticScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ARCTICSCAN, "arcticscan.top") {
	override val datePattern: String = "yyyy-MM-dd"
}
