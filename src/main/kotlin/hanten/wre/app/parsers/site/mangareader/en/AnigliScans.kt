package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("ANIGLISCANS", "AnigliScans", "en")
internal class AnigliScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.ANIGLISCANS, "anigliscans.xyz", pageSize = 47, searchPageSize = 47) {
	override val listUrl = "/series"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
