package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGA_QUEEN", "MangaQueen", "en")
internal class MangaQueen(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGA_QUEEN, "mangaqueen.com", 16)
