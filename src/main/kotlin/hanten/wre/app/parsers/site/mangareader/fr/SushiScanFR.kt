package hanten.wre.app.parsers.site.mangareader.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SUSHISCANFR", "SushiScan.fr", "fr")
internal class SushiScanFR(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SUSHISCANFR, "sushiscan.fr", pageSize = 36, searchPageSize = 10) {
	override val listUrl = "/catalogue"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
