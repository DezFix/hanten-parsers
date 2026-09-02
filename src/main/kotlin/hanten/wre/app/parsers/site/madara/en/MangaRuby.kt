package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGARUBY", "MangaRuby", "en")
internal class MangaRuby(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGARUBY, "mangaruby.com", 10)
