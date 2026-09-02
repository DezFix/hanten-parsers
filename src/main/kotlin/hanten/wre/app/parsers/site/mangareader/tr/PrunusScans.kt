package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("PRUNUSSCANS", "PrunusScans", "tr")
internal class PrunusScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.PRUNUSSCANS, "prunusscans.com", pageSize = 20, searchPageSize = 10)
