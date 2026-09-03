package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("KOMIKINDO_LIVE", "Komikindo.live", "id", ContentType.HENTAI)
internal class KomikindoLive(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.KOMIKINDO_LIVE, "komikindo.live", pageSize = 20, searchPageSize = 10) {
	override val datePattern = "MMMM d, yyyy"
}
