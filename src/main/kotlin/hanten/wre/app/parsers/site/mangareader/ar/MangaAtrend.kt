package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGAATREND", "MangaAtrend", "ar")
internal class MangaAtrend(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGAATREND, "mangaatrend.net", pageSize = 30, searchPageSize = 10)
