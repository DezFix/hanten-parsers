package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("MANGASUSUKU", "MangaSusuku", "id", ContentType.HENTAI)
internal class MangaSusuku(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGASUSUKU, "mangasusuku.com", pageSize = 20, searchPageSize = 20) {
	override val listUrl = "/komik"
	override val datePattern = "MMM d, yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
	override val isNetShieldProtected = true
}
