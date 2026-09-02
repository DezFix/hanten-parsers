package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("WONDERLANDSCAN", "WonderlandScan", "pt")
internal class WonderlandScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.WONDERLANDSCAN, "wonderlandscan.com") {
	override val datePattern: String = "dd/MM/yyyy"
}
