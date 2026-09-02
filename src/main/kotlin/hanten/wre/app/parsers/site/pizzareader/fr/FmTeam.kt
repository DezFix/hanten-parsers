package hanten.wre.app.parsers.site.pizzareader.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.pizzareader.PizzaReaderParser

@MangaSourceParser("FMTEAM", "FmTeam", "fr")
internal class FmTeam(context: MangaLoaderContext) :
	PizzaReaderParser(context, MangaParserSource.FMTEAM, "fmteam.fr") {
	override val ongoingFilter = "en cours"
	override val completedFilter = "terminé"
}
