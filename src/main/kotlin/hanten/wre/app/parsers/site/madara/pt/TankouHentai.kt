package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TANKOUHENTAI", "TankouHentai", "pt", ContentType.HENTAI)
internal class TankouHentai(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TANKOUHENTAI, "tankouhentai.com", pageSize = 16) {
	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
