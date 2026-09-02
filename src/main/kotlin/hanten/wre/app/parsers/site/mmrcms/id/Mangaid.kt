package hanten.wre.app.parsers.site.mmrcms.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mmrcms.MmrcmsParser
import java.util.*

@Broken
@MangaSourceParser("MANGAID", "MangaId", "id")
internal class Mangaid(context: MangaLoaderContext) :
	MmrcmsParser(context, MangaParserSource.MANGAID, "mangaid.click") {
	override val selectState = "dt:contains(Status)"
	override val selectAlt = "dt:contains(Other names)"
	override val selectAut = "dt:contains(Author(s))"
	override val selectTag = "dt:contains(Categories)"
	override val sourceLocale: Locale = Locale.ENGLISH
}
