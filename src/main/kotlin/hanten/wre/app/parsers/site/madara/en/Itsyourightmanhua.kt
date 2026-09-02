package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ITSYOURIGHTMANHUA", "ItsYouRightManhua", "en")
internal class Itsyourightmanhua(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ITSYOURIGHTMANHUA, "itsyourightmanhua.com", 10)
