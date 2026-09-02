package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("PIANMANGA", "PianManga", "en")
internal class PianManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PIANMANGA, "pianmanga.me", pageSize = 10) {
	override val datePattern = "MMMM dd, yyyy"
}
