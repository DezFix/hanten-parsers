package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TOONILY", "Toonily", "en")
internal class Toonily(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TOONILY, "toonily.com", pageSize = 18) {
	override val listUrl = "webtoon/"
	override val tagPrefix = "webtoon-genre/"
	override val datePattern = "MMMM dd, yyyy"
}
