package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("ELFTOON", "Elftoon", "en")
internal class Elftoon(context: MangaLoaderContext) :
    MangaReaderParser(context, MangaParserSource.ELFTOON, "elftoon.com", pageSize = 20, searchPageSize = 10)
