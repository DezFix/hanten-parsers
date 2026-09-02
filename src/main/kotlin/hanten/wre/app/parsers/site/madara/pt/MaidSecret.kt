package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MAIDSECRET", "MaidSecret", "pt")
internal class MaidSecret(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MAIDSECRET, "maidsecret.com", 10) {
	override val datePattern: String = "dd/MM/yyyy"
}
