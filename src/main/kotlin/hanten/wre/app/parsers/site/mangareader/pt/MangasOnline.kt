package hanten.wre.app.parsers.site.mangareader.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("MANGASONLINE", "MangasOnline", "pt")
internal class MangasOnline(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGASONLINE, "mangasonline.cc", pageSize = 20, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
