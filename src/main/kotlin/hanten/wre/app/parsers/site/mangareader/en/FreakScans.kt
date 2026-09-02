package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("FREAKSCANS", "FreakScans", "en")
internal class FreakScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.FREAKSCANS, "freakscans.com", pageSize = 20, searchPageSize = 20) {
	override val datePattern = "MMM d, yyyy"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
