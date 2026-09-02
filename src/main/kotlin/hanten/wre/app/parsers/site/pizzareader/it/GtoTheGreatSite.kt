package hanten.wre.app.parsers.site.pizzareader.it

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.pizzareader.PizzaReaderParser

@MangaSourceParser("GTOTHEGREATSITE", "GtoTheGreatSite", "it")
internal class GtoTheGreatSite(context: MangaLoaderContext) :
	PizzaReaderParser(context, MangaParserSource.GTOTHEGREATSITE, "reader.gtothegreatsite.net")
