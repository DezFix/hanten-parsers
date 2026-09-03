package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("MASTERKOMIK", "Tenshi", "id")
internal class MasterKomik(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MASTERKOMIK, "tenshi01.id", pageSize = 20, searchPageSize = 20) {
	override val datePattern = "MMM d, yyyy"
	override val listUrl = "/komik"
}
