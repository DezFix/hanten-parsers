package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("MANGASIGINAGI", "MangaSiginagi", "tr")
internal class MangaSiginagi(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGASIGINAGI, "mangasiginagi.com", pageSize = 20, searchPageSize = 10)
