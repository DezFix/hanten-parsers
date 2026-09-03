package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("NIGHTSCANS", "NightScans", "en")
internal class Nightscans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.NIGHTSCANS, "nightsup.net", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/series"
	override val selectMangaListImg = "img.ts-post-image, picture img"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
