package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("FLARES", "Fl-Ares", "ar")
internal class FlAres(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.FLARES, "fl-ares.com", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/series"
	override val encodedSrc = true
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
