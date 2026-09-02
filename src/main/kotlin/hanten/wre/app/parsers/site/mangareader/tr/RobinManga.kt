package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("ROBINMANGA", "RobinManga", "tr")
internal class RobinManga(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.ROBINMANGA,
		"www.guildknives.com",
		pageSize = 20,
		searchPageSize = 25,
	) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
