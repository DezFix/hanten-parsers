package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("LUACOMIC_COM", "luaComic.com", "en")
internal class LuaComicCom(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.LUACOMIC_COM, "luascans.com", pageSize = 20, searchPageSize = 10) {
	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
