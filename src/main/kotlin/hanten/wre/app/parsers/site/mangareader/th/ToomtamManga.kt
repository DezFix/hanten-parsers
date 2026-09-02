package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("TOOMTAMMANGA", "ToomtamManga", "th", ContentType.HENTAI)
internal class ToomtamManga(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.TOOMTAMMANGA,
		"toomtam-manga.com",
		pageSize = 30,
		searchPageSize = 28,
	) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
