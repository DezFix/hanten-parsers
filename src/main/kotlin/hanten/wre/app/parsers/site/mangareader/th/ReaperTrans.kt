package hanten.wre.app.parsers.site.mangareader.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*

@MangaSourceParser("REAPERTRANS", "ReaperTrans", "th")
internal class ReaperTrans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.REAPERTRANS, "reapertrans.com", pageSize = 30, searchPageSize = 14) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
	override val sourceLocale: Locale = Locale.ENGLISH
}
