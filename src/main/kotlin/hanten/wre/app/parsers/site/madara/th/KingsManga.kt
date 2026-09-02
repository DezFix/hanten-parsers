package hanten.wre.app.parsers.site.madara.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("KINGS_MANGA", "SuperDoujin", "th", ContentType.HENTAI)
internal class KingsManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.KINGS_MANGA, "www.superdoujin.org") {
	override val postReq = true
	override val datePattern = "d MMMM yyyy"
}
