package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("CREEPYSCANS", "CreepyScans", "en")
internal class CreepyScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.CREEPYSCANS, "creepyscans.com") {
	override val stylePage = ""
}
