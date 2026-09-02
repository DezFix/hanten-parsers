package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("INFAMOUSSCANS", "InfamousScans", "en")
internal class InfamousScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.INFAMOUSSCANS, "infamous-scans.com", 10)
