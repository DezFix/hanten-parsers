package hanten.wre.app.parsers.site.cupfox.ja

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.cupfox.CupFoxParser

@MangaSourceParser("MANGAKOINU", "MangaKoinu", "ja")
internal class MangaKoinu(context: MangaLoaderContext) :
	CupFoxParser(context, MangaParserSource.MANGAKOINU, "www.mangakoinu.com")
