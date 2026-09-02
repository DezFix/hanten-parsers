package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("DIAMONDFANSUB", "DiamondFansub", "tr")
internal class DiamondFansub(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DIAMONDFANSUB, "diamondfansub.com", 10) {
	override val datePattern = "d MMMM"
	override val listUrl = "seri/"
	override val tagPrefix = "seri-turu/"
}
