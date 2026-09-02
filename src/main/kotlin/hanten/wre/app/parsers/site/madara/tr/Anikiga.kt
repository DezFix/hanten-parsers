package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("ANIKIGA", "Anikiga", "tr")
internal class Anikiga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ANIKIGA, "anikiga.com") {
	override val tagPrefix = "manga-tur/"
	override val datePattern = "d MMMM yyyy"
	override val postReq = true
}
