package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("EUPHORIASCAN", "EuphoriaScan", "pt", ContentType.HENTAI)
internal class EuphoriaScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.EUPHORIASCAN, "euphoriascan.com", 10) {
	override val datePattern: String = "dd 'de' MMMM 'de' yyyy"
}
