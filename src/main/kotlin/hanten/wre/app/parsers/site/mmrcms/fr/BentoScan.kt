package hanten.wre.app.parsers.site.mmrcms.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mmrcms.MmrcmsParser
import java.util.*

@Broken
@MangaSourceParser("BENTOSCAN", "BentoScan", "fr")
internal class BentoScan(context: MangaLoaderContext) :
	MmrcmsParser(context, MangaParserSource.BENTOSCAN, "bentoscan.com") {
	override val sourceLocale: Locale = Locale.ENGLISH
	override val imgUpdated = ".jpg"
}
