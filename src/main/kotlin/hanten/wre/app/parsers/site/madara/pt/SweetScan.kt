package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("SWEETSCAN", "SweetScan", "pt")
internal class SweetScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SWEETSCAN, "sweetscan.net") {
	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
