package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAMANIACS", "MangaManiacs", "en", ContentType.HENTAI)
internal class MangaManiacs(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAMANIACS, "mangamaniacs.org", 10)
