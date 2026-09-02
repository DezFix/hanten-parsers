package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken("Original site closed")
@MangaSourceParser("SCANSRAW", "AquaScans.com", "en")
internal class Scansraw(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SCANSRAW, "aquascans.com", 10)
