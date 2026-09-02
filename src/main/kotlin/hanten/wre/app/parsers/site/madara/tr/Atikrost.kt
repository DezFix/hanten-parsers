package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ATIKROST", "Atikrost", "tr")
internal class Atikrost(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ATIKROST, "www.mangaoku.org", 10) {
	override val datePattern = "d MMMM yyyy"
}
