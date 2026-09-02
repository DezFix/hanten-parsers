package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@Broken
@MangaSourceParser("KOMIKLOKALCFD", "KomikLokal.mom", "id", ContentType.HENTAI)
internal class KomiklokalCfd(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.KOMIKLOKALCFD, "komikmu.icu", pageSize = 30, searchPageSize = 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
}