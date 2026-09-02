package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SHOJOSCANS", "ShojoScans", "en")
internal class ShojoScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SHOJOSCANS, "violetscans.com", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/comics"
}
