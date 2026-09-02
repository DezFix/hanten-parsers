package hanten.wre.app.parsers.site.madara.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken
import java.util.*

@Broken
@MangaSourceParser("MANHUAKEY", "ManhuaKey", "th")
internal class Manhuakey(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHUAKEY, "www.manhuakey.com", 10) {
	override val datePattern: String = "d MMMM yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
	override val selectPage = "img"
}
