package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MI2MANGAES", "Mi2MangaEs", "es")
internal class Mi2MangaEs(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MI2MANGAES, "mi2manga.lat", 10)
