package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("LAGOONSCANS", "Lagoon Scans", "en")
internal class LagoonScans(context: MangaLoaderContext) :
    MangaReaderParser(context, MangaParserSource.LAGOONSCANS, "lagoonscans.com", pageSize = 20, searchPageSize = 10)
