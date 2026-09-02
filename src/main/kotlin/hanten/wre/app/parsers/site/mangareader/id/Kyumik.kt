package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("KYUMIK", "Kyumik", "id", ContentType.HENTAI)
internal class Kyumik(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.KYUMIK, "kyumik.com", pageSize = 20, searchPageSize = 10)