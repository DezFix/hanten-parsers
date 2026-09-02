package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@Broken
@MangaSourceParser("OTSUGAMI", "Otsugami", "id")
internal class Otsugami(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.OTSUGAMI, "otsugami.id", pageSize = 40, searchPageSize = 10) {
	override val sourceLocale: Locale = Locale.ENGLISH

}
