package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MAFIAMANGA", "MafiaManga", "th")
internal class MafiaManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MAFIAMANGA, "mafia-manga.com", pageSize = 20, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
