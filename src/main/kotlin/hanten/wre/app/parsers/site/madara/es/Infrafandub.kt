package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("INFRAFANDUB", "InfraFandub", "es")
internal class Infrafandub(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.INFRAFANDUB, "infrafandub.com", 10) {
	override val datePattern = "dd/MM/yyyy"
}
