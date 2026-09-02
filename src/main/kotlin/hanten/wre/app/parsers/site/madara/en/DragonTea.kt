package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("DRAGONTEA", "DragonTea", "en")
internal class DragonTea(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DRAGONTEA, "dragontea.ink") {
	override val datePattern = "MM/dd/yyyy"
}
