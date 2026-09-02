package hanten.wre.app.parsers.site.madara.ru

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAMAMMY", "MangaMammy", "ru")
internal class MangaMammy(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAMAMMY, "mangamammy.ru") {
	override val datePattern = "dd.MM.yyyy"
	override val postReq = true
}
