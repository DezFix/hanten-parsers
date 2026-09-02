package hanten.wre.app.parsers.site.pizzareader.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.pizzareader.PizzaReaderParser

@MangaSourceParser("HNISCANTRAD", "HniScantrad", "fr")
internal class HniScantrad(context: MangaLoaderContext) :
	PizzaReaderParser(context, MangaParserSource.HNISCANTRAD, "hni-scantrad.net") {
	override val ongoingFilter = "en cours"
	override val completedFilter = "terminé"
}
