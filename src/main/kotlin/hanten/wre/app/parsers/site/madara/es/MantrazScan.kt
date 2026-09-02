package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANTRAZSCAN", "MantrazScan", "es")
internal class MantrazScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANTRAZSCAN, "mantrazscan.org") {
	override val datePattern = "dd/MM/yyyy"
	override val tagPrefix = "generos-de-manga/"
}
