package hanten.wre.app.parsers.site.mangareader.pl

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SKANLACJEFENIKSY", "SkanlacjeFeniksy", "pl")
internal class SkanlacjeFeniksy(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.SKANLACJEFENIKSY,
		"skanlacje-feniksy.pl",
		pageSize = 10,
		searchPageSize = 10,
	) {
	override val datePattern = "d MMMM, yyyy"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
