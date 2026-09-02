package hanten.wre.app.parsers.site.onemanga.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.onemanga.OneMangaParser

@MangaSourceParser("BERSERKSCAN", "BerserkScan", "fr")
internal class BerserkScan(context: MangaLoaderContext) :
	OneMangaParser(context, MangaParserSource.BERSERKSCAN, "berserkscan.com")
