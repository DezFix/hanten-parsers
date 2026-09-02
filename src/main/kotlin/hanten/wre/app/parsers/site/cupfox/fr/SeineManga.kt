package hanten.wre.app.parsers.site.cupfox.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.cupfox.CupFoxParser

@MangaSourceParser("SEINEMANGA", "SeineManga", "fr")
internal class SeineManga(context: MangaLoaderContext) :
	CupFoxParser(context, MangaParserSource.SEINEMANGA, "www.seinemanga.com")
