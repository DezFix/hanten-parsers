package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("MANGACIM", "Mangacim", "tr")
internal class Mangacim(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGACIM, "mangacim.com.tr", pageSize = 20, searchPageSize = 20) {
	override val datePattern = "MMM d, yyyy"
}
