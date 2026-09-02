package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MOMONOHANASCAN", "MomonohanaScan", "pt")
internal class MomonohanaScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MOMONOHANASCAN, "momonohanascan.com", 10) {
	override val datePattern: String = "dd/MM/yyyy"
}
