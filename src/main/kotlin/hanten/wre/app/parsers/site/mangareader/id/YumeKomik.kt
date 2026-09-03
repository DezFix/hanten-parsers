package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("YUMEKOMIK", "YumeKomik", "id")
internal class YumeKomik(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.YUMEKOMIK, "yumekomik.com", pageSize = 20, searchPageSize = 10)
