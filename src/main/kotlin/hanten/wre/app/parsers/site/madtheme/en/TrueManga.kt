package hanten.wre.app.parsers.site.madtheme.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madtheme.MadthemeParser

@Broken("Down forever")
@MangaSourceParser("TRUEMANGA", "TrueManga", "en")
internal class TrueManga(context: MangaLoaderContext) :
	MadthemeParser(context, MangaParserSource.TRUEMANGA, "mangamonk.com")
