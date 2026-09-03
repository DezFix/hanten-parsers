package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("EROSSCANS", "ErosScans", "en")
internal class ErosScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.EROSSCANS, "erosxscans.xyz", pageSize = 20, searchPageSize = 10)
