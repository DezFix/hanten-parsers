package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("GUNCEL_MANGA", "GuncelManga", "tr")
internal class GuncelManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.GUNCEL_MANGA, "guncelmanga.net") {
	override val datePattern = "d MMMM yyyy"
}
