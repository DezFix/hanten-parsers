package hanten.wre.app.parsers.site.liliana.ja

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.liliana.LilianaParser

@MangaSourceParser("RAW1001", "Raw1001", "ja")
internal class Raw1001(context: MangaLoaderContext) :
	LilianaParser(context, MangaParserSource.RAW1001, "raw1001.net")
