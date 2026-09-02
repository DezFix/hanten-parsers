package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MUNDO_MANHWA", "MundoManhwa", "es")
internal class MundoManhwa(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MUNDO_MANHWA, "mundomanhwa.com", 10)
