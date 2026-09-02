package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("FREEMANGA", "FreeManga", "en")
internal class FreeManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.FREEMANGA, "freemanga.me") {
	override val datePattern = "MMMM dd, yyyy"
}
