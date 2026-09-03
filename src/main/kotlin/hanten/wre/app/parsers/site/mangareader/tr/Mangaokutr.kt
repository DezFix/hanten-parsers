package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGAOKUTR", "MangaOkuTr", "tr")
internal class Mangaokutr(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGAOKUTR, "mangaokutr.net", pageSize = 25, searchPageSize = 20)
