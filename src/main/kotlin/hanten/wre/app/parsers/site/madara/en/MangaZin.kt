package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAZIN", "MangaZin", "en")
internal class MangaZin(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAZIN, "mangazin.org")
