package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken("Original site closed")
@MangaSourceParser("HYPERIONSCANS", "SeraphManga", "tr")
internal class HyperionScans(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.HYPERIONSCANS,
		"www.seraphmanga.com",
		pageSize = 20,
		searchPageSize = 10,
	) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
