package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("FURYMANGA", "KingOfScans", "en")
internal class FuryManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.FURYMANGA, "myshojo.com", pageSize = 30, searchPageSize = 10) {
	override val listUrl = "/comics"
}
