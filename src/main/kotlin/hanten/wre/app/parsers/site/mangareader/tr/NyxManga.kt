package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("NYXMANGA", "NyxManga", "tr")
internal class NyxManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.NYXMANGA, "nyxmanga.com", pageSize = 14, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}