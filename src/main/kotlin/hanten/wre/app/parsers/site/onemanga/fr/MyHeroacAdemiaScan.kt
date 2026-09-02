package hanten.wre.app.parsers.site.onemanga.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.onemanga.OneMangaParser

@MangaSourceParser("MYHEROACADEMIASCAN", "MyHeroacAdemiaScan", "fr")
internal class MyHeroacAdemiaScan(context: MangaLoaderContext) :
	OneMangaParser(context, MangaParserSource.MYHEROACADEMIASCAN, "myheroacademiascan.com")
