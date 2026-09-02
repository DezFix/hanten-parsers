package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANHWADESU", "ManhwaDesu", "id", ContentType.HENTAI)
internal class ManhwadesuParser(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANHWADESU, "manhwadesu.asia", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/komik"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
