package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHUAGA", "ManhuaGa", "en")
internal class Manhuaga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHUAGA, "manhuaga.com")
