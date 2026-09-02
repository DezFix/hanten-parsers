package hanten.wre.app.parsers.site.foolslide.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.foolslide.FoolSlideParser

@MangaSourceParser("DEATHTOLLSCANS", "DeathTollScans", "en")
internal class Deathtollscans(context: MangaLoaderContext) :
	FoolSlideParser(context, MangaParserSource.DEATHTOLLSCANS, "reader.deathtollscans.net", 26)
