package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("LECTORUNITOON", "LectoruniToon", "es")
internal class Lectorunitoon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LECTORUNITOON, "lectorunitoon.com", 10) {
	override val tagPrefix = "generos/"
	override val datePattern = "dd/MM/yyyy"
}
