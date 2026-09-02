package hanten.wre.app.parsers.site.mangareader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("TRADUCCIONESMOONLIGHT", "TraduccionesMoonlight", "es", ContentType.HENTAI)
internal class TraduccionesMoonlight(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.TRADUCCIONESMOONLIGHT, "traduccionesmoonlight.com", 20, 10)
