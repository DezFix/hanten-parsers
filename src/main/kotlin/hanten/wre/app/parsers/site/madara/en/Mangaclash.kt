package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGACLASH", "ToonClash", "en")
internal class Mangaclash(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGACLASH, "toonclash.com", pageSize = 18) {
	override val datePattern = "MM/dd/yyyy"
    override val withoutAjax = true
}
