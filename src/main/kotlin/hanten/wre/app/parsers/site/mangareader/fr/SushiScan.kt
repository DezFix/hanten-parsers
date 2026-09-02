package hanten.wre.app.parsers.site.mangareader.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken("Blocked by Cloudflare")
@MangaSourceParser("SUSHISCAN", "SushiScan.Net", "fr")
internal class SushiScan(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SUSHISCAN, "sushiscan.net", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/catalogue"
	override val datePattern = "MMM d, yyyy"
}
