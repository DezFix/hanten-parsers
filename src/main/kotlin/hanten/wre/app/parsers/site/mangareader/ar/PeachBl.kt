package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("PEACHBL", "PeachBl", "ar", ContentType.HENTAI)
internal class PeachBl(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.PEACHBL, "peach-bl.com", pageSize = 20, searchPageSize = 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
}
