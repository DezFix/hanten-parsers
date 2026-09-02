package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken // The website is either closed or constantly blocked
@MangaSourceParser("SIIKOMIK", "SiiKomik", "id")
internal class SiiKomik(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SIIKOMIK, "siikomik.fun", pageSize = 20, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
			isSearchSupported = false,
		)
}

