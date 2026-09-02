package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Content not found or removed")
@MangaSourceParser("MILASUB", "MilaSub", "tr")
internal class MilaSub(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MILASUB, "milascans.tr", 20) {
	override val datePattern = "d MMMM yyyy"
}
