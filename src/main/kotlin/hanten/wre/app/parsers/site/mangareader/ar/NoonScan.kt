package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("NOONSCAN", "NoonScan.com", "ar")
internal class NoonScan(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.NOONSCAN, "noonscan.com", pageSize = 20, searchPageSize = 10)
