package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("YURILIVE", "YuriLive", "pt", ContentType.HENTAI)
internal class YuriLive(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YURILIVE, "yuri.live") {
	override val tagPrefix = "manga-genero/"
	override val datePattern: String = "dd/MM/yyyy"
}
