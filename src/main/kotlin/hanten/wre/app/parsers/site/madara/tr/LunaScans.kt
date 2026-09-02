package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("LUNASCANS", "LunaScans", "tr", ContentType.HENTAI)
internal class LunaScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LUNASCANS, "tuhafscans.com") {
	override val postReq = true
	override val datePattern = "dd MMMM yyyy"
}
