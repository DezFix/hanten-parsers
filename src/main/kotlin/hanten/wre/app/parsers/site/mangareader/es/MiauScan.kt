package hanten.wre.app.parsers.site.mangareader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MIAUSCAN", "LectorMiau", "es")
internal class MiauScan(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MIAUSCAN, "leemiau.com", pageSize = 20, searchPageSize = 10)
