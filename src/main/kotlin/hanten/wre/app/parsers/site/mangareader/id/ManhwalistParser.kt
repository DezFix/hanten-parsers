package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@Broken("maintenance for an unspecified time")
@MangaSourceParser("MANHWALIST", "ManhwaList", "id")
internal class ManhwalistParser(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANHWALIST, "manhwalist.xyz", pageSize = 24, searchPageSize = 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
}
