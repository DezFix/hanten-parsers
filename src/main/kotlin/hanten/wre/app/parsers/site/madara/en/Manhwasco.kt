package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWASCO", "ManhwaSco", "en")
internal class Manhwasco(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWASCO, "manhwasco.net") {
	override val selectGenre = "div.mg_genres a"
}
