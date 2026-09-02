package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("THAIMANGA", "ThaiManga", "th")
internal class ThaiManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.THAIMANGA, "www.thaimanga.net", pageSize = 40, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
