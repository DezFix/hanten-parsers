package hanten.wre.app.parsers.site.mangareader.cz

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("EVILMANGA", "EvilManga", "cs", ContentType.HENTAI)
internal class EvilManga(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.EVILMANGA, "evil-manga.eu", pageSize = 20, searchPageSize = 10) {
	override val datePattern = "d MMMM, yyyy"
}
