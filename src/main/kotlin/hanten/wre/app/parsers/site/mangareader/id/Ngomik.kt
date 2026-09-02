package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("NGOMIK", "Ngomik", "id")
internal class Ngomik(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.NGOMIK, "ngomik.mom", pageSize = 20, searchPageSize = 5) {
	override val sourceLocale: Locale = Locale.ENGLISH
}
