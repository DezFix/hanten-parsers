package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("DOUJIN69", "Doujin69", "th", type = ContentType.HENTAI)
internal class Doujin69(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.DOUJIN69, "doujin69.com", pageSize = 40, searchPageSize = 21) {
	override val listUrl = "/doujin"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
