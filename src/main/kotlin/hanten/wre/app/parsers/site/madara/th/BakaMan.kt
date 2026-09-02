package hanten.wre.app.parsers.site.madara.th

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("BAKAMAN", "BakaMan", "th")
internal class BakaMan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BAKAMAN, "bakaman.net", pageSize = 18) {
	override val datePattern = "MMMM dd, yyyy"
}
