package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("LECTORMANGA", "LectorManga", "es")
internal class LectorManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LECTORMANGA, "lectormangaa.com") {
	override val listUrl = "biblioteca/"
	override val tagPrefix = "comics-genero/"
}
