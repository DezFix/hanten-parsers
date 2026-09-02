package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("LILYMANGA", "LilyManga", "en", ContentType.HENTAI)
internal class LilyManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LILYMANGA, "lilymanga.net") {
	override val tagPrefix = "ys-genre/"
	override val listUrl = "ys/"
	override val datePattern = "yyyy-MM-dd"
}
