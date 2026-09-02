package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGA1ST", "Manga1st", "en")
internal class Manga1st(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGA1ST, "manga1st.online") {
	override val datePattern = "d MMMM، yyyy"
}
