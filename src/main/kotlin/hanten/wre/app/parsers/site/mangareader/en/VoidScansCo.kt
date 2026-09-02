package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken
@MangaSourceParser("VOIDSCANS_CO", "VoidScans", "en")
internal class VoidScansCo(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.VOIDSCANS_CO, "voidscans.co", pageSize = 30, searchPageSize = 42)
