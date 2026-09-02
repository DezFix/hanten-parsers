package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("LEVIATANSCANS", "LsComic", "en")
internal class LeviatanScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LEVIATANSCANS, "lscomic.com", 10)
