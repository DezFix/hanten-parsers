package hanten.wre.app.parsers.site.onemanga.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.onemanga.OneMangaParser

@MangaSourceParser("ONEPUNCHMANSCAN", "OnePunchManScan", "fr")
internal class OnePunchManScan(context: MangaLoaderContext) :
	OneMangaParser(context, MangaParserSource.ONEPUNCHMANSCAN, "onepunchmanscan.com")
