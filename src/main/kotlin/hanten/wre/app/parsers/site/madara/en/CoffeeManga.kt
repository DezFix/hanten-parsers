package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("COFFEE_MANGA", "CoffeeManga", "en")
internal class CoffeeManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.COFFEE_MANGA, "coffeemanga.io")
