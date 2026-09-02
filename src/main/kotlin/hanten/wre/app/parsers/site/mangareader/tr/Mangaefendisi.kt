package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGAEFENDISI", "MangaEfendisi", "tr")
internal class Mangaefendisi(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.MANGAEFENDISI,
		"mangaefendisi.net",
		pageSize = 30,
		searchPageSize = 20,
	) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
