package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAFOXFULL", "MangaFoxFull", "en")
internal class MangaFoxFull(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAFOXFULL, "mangafoxfull.com") {
	override val postReq = true
}
