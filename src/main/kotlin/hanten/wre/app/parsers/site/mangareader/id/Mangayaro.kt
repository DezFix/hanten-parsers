package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("MANGAYARO", "MangaYaro", "id")
internal class Mangayaro(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.MANGAYARO,
		"mangayaro.id",
		pageSize = 20,
		searchPageSize = 20,
	) {
	override val sourceLocale: Locale = Locale.ENGLISH
}
