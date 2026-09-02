package hanten.wre.app.parsers.site.mangareader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("ASIALOTUSS", "AsiaLotuss", "es")
internal class AsiaLotuss(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.ASIALOTUSS, "asialotuss.com", pageSize = 20, searchPageSize = 10)
