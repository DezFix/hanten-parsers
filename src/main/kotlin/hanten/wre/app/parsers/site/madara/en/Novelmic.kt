package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("NOVELMIC", "NovelMic", "en")
internal class Novelmic(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.NOVELMIC, "novelmic.com", 20) {
	override val postReq = true
}
