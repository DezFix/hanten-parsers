package hanten.wre.app.parsers.site.madara.ar

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGAPEAK", "MangaPeak", "ar")
internal class MangaPeak(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAPEAK, "mangapeak.org")
