package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("MANGAKAZANI", "MangaKazani", "tr")
internal class MangaKazani(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGAKAZANI, "mangakazani.com", pageSize = 19, searchPageSize = 10) {
	override val listUrl = "/seriler"
}
