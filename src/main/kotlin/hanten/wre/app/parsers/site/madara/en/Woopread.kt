package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("WOOPREAD", "Woopread", "en")
internal class Woopread(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.WOOPREAD, "woopread.com", 10) {
	override val listUrl = "series/"
	override val tagPrefix = "series-genres/"
}
