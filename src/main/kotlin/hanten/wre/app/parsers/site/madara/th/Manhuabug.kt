package hanten.wre.app.parsers.site.madara.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@MangaSourceParser("MANHUABUG", "ManhuaBug", "th")
internal class Manhuabug(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHUABUG, "www.manhuabug.com", 10) {
	override val datePattern: String = "d MMMM yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
	override val selectPage = "img"
}
