package hanten.wre.app.parsers.site.madtheme.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madtheme.MadthemeParser

@Broken("Website down forever")
@MangaSourceParser("MANGABUDDY", "MangaBuddy", "en")
internal class MangaBuddy(context: MangaLoaderContext) :
	MadthemeParser(context, MangaParserSource.MANGABUDDY, "mangabuddy.com")
