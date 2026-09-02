package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGASUSHI", "MangaSushi", "en")
internal class MangaSushi(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGASUSHI, "mangasushi.org")
