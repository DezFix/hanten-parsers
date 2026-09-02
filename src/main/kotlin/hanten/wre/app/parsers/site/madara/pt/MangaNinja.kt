package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGANINJA", "MangaNinja", "pt")
internal class MangaNinja(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGANINJA, "manganinja.com", 10) {
	override val datePattern: String = "dd/MM/yyyy"
}
