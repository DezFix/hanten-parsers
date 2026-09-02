package hanten.wre.app.parsers.site.mangareader.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("STELLARSABER", "StellarSaber", "ar")
internal class StellarSaber(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.STELLARSABER, "stellarsaber.pro", pageSize = 32, searchPageSize = 10)
