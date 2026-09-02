package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("IZANAMISCANS", "IzanamiScans", "id")
internal class IzanamiScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.IZANAMISCANS, "izanamiscans.my.id", pageSize = 20, searchPageSize = 10)
