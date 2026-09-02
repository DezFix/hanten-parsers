package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("READERSPOINT", "ReadersPoint", "en")
internal class ReadersPoint(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.READERSPOINT, "qscomics.org", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/series"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
