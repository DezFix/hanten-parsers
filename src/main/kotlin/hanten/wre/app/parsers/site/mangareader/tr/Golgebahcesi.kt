package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("GOLGEBAHCESI", "GolgeBahcesi", "tr")
internal class Golgebahcesi(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.GOLGEBAHCESI, "golgebahcesi.com", pageSize = 14, searchPageSize = 9) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
