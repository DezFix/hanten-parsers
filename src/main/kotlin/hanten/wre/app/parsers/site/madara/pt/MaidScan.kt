package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MAIDSCAN", "MaidScan", "pt", ContentType.HENTAI)
internal class MaidScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MAIDSCAN, "novo.empreguetes.site", 10) {
	override val datePattern: String = "dd/MM/yyyy"
}
