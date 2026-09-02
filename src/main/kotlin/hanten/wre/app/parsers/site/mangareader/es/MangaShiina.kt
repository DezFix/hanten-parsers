package hanten.wre.app.parsers.site.mangareader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGASHIINA", "MangaMukai.com", "es")
internal class MangaShiina(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGASHIINA, "mangamukai.com", pageSize = 20, searchPageSize = 10)
