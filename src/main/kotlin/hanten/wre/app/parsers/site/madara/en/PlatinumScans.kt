package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("PLATINUMSCANS", "PlatinumScans", "en")
internal class PlatinumScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PLATINUMSCANS, "platinumscans.com", pageSize = 10) {
	override val postReq = true
}
