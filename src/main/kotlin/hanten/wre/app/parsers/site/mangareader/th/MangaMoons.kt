package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("MANGAMOONS", "MangaMoons", "th")
internal class MangaMoons(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGAMOONS, "manga-moons.net", pageSize = 20, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
