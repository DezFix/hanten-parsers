package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("ECCHIDOUJIN", "EcchiDoujin", "th", ContentType.HENTAI)
internal class EcchiDoujin(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.ECCHIDOUJIN, "ecchi-doujin.com", pageSize = 30, searchPageSize = 10) {
	override val listUrl = "/doujin"
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
