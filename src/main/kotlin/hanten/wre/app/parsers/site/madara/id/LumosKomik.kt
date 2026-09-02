package hanten.wre.app.parsers.site.madara.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@MangaSourceParser("LUMOSKOMIK", "LumosKomik", "id")
internal class LumosKomik(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LUMOSKOMIK, "lumos01.com") {
	override val tagPrefix = "genre/"
	override val listUrl = "komik/"
	override val datePattern = "dd MMMM yyyy"
	override val sourceLocale: Locale = Locale.ENGLISH
}
