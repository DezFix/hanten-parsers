package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("BABELWUXIA", "Babelwuxia", "en")
internal class Babelwuxia(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BABELWUXIA, "babelwuxia.com")
