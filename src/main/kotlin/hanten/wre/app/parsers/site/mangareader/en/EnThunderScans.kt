package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("ENTHUNDERSCANS", "EnThunderScans", "en")
internal class EnThunderScans(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.ENTHUNDERSCANS,
		"en-thunderscans.com",
		pageSize = 30,
		searchPageSize = 10,
	) {
	override val listUrl = "/comics"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
