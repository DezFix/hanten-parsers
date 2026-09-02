package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("MANGAGOJO", "MangaGojo", "en")
internal class MangaGojo(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.MANGAGOJO, "mangagojo.com", 30, 20)
