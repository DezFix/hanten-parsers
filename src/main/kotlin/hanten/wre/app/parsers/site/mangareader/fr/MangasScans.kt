package hanten.wre.app.parsers.site.mangareader.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGASSCANS", "MangasScans", "fr")
internal class MangasScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGASSCANS, "mangas-scans.com", pageSize = 30, searchPageSize = 10)
