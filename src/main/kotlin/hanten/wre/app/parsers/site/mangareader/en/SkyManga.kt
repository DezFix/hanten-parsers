package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("SKY_MANGA", "SkyManga", "en")
internal class SkyManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SKY_MANGA, "skymanga.work", pageSize = 20, searchPageSize = 20) {
	override val listUrl = "/manga-list"
	override val datePattern = "dd-MM-yyy"
}
