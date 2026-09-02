package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAFREAK", "MangaFreak", "en")
internal class MangaFreak(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAFREAK, "mangafreak.online") {
	override val postReq = true
	override val datePattern = "dd MMMM، yyyy"
}
