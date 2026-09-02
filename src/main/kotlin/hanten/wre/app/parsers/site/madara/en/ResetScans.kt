package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("RESETSCANS", "Resetscans", "en")
internal class ResetScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.RESETSCANS, "reset-scans.org", 20)

