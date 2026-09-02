package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("NABISCANS", "NabiScans", "tr")
internal class NabiScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.NABISCANS, "nabiscans.com") {
	override val datePattern = "d MMMM yyyy"
}
