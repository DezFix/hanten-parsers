package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

//This source requires an account.
@MangaSourceParser("OPIATOON", "OpiaToon", "tr")
internal class OpiaToon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.OPIATOON, "opiatoon.art", 20) {
	override val datePattern = "d MMMM"
}
