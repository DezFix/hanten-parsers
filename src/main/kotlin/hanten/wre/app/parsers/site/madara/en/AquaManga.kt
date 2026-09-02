package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("AQUAMANGA", "AquaManga", "en")
internal class AquaManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.AQUAMANGA, "aquareader.net", 20)
