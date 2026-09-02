package hanten.wre.app.parsers.site.mangareader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.Locale
import hanten.wre.app.parsers.Broken

@Broken("Not dead, changed template")
@MangaSourceParser("CATHARSISWORLD", "CatharsisWorld", "es")
internal class CatharsisWorld(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.CATHARSISWORLD, "catharsisworld.dig-it.info", pageSize = 30, searchPageSize = 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
