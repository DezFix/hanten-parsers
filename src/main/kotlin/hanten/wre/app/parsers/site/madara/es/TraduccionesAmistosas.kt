package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TRADUCCIONESAMISTOSAS", "TraduccionesAmistosas", "es")
internal class TraduccionesAmistosas(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TRADUCCIONESAMISTOSAS, "traduccionesamistosas.topmanhuas.org", 10) {
	override val datePattern = "d 'de' MMMMM 'de' yyyy"
}
