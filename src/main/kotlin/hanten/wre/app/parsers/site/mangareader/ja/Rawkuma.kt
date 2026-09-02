package hanten.wre.app.parsers.site.mangareader.ja

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import java.util.*
import hanten.wre.app.parsers.Broken

@Broken("Need to rewrite parser, continue with rawkuma.net site, not old.rawkuma.net")
@MangaSourceParser("RAWKUMA", "Rawkuma", "ja")
internal class Rawkuma(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.RAWKUMA, "old.rawkuma.net", pageSize = 54, searchPageSize = 54) {
	override val datePattern = "MMM d, yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
