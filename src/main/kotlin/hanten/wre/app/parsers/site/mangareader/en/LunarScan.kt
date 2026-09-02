package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("LUNAR_SCAN", "LunarScan.org", "en", ContentType.HENTAI)
internal class LunarScan(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.LUNAR_SCAN, "lunarscan.org", pageSize = 20, searchPageSize = 20) {
	override val listUrl = "/series"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
