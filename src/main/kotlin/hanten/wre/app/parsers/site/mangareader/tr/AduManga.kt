package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("ADUMANGA", "AduManga", "tr")
internal class AduManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.ADUMANGA, "adumanga.com", pageSize = 20, searchPageSize = 10) {

	override val sourceLocale: Locale = Locale.ENGLISH

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
