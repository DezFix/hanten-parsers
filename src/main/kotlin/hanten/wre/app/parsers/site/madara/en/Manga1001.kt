package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGA1001", "Manga1001", "en")
internal class Manga1001(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGA1001, "manga-1001.com", 10)
