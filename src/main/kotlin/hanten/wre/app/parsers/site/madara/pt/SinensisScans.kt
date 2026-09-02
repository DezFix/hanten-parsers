package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Not dead, changed template")
@MangaSourceParser("SINENSISSCANS", "SinensisScans", "pt")
internal class SinensisScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SINENSISSCANS, "sinensis.leitorweb.com") {
	override val datePattern: String = "dd/MM/yyyy"
}
