package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TCBSCANSMANGA", "TcbScansManga", "en")
internal class TcbScansManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TCBSCANSMANGA, "tcbscans-manga.com", 10) {
	override val selectPage = "img"
}
