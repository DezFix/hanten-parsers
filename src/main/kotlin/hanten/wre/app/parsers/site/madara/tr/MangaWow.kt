package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAWOW", "MangaWow", "tr")
internal class MangaWow(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAWOW, "mangawow.org", 18) {
	override val datePattern = "d MMMM yyyy"
}
