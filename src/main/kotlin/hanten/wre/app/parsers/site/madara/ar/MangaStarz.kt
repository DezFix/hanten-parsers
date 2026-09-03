package hanten.wre.app.parsers.site.madara.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGASTARZ", "Manga-Starz", "ar")
internal class MangaStarz(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGASTARZ, "manga-starz.net", pageSize = 10) {
	override val datePattern = "d MMMM، yyyy"
	override val stylePage = ""
    override val withoutAjax = true
}
