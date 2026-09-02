package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("VERMANHWA", "Vermanhwa", "es", ContentType.HENTAI)
internal class Vermanhwa(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.VERMANHWA, "vermanhwa.com", 10)
