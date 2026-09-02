package hanten.wre.app.parsers.site.mangareader.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SILENCESCAN", "SilenceScan", "pt", ContentType.HENTAI)
internal class Silencescan(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.SILENCESCAN,
		"silencescan.com.br",
		pageSize = 35,
		searchPageSize = 35,
	) {
	override val datePattern = "MMM d, yyyy"
}
