package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MURIMSCAN", "InkReads", "en")
internal class MurimScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MURIMSCAN, "inkreads.com", 100) {
	override val postReq = true
	override val listUrl = "mangax/"
}
