package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SHIJIESCANS", "ShijieScans", "tr")
internal class ShijieScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SHIJIESCANS, "shijiescans.com", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/seri"
}
