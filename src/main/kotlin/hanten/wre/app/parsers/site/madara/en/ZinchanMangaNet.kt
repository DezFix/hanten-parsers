package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ZINCHANMANGA_NET", "ZinchanManga.net", "en")
internal class ZinchanMangaNet(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ZINCHANMANGA_NET, "zinchangmanga.net", 10)
