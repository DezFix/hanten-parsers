package hanten.wre.app.parsers.site.madara.th

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("CAT_300", "Cat300", "th", ContentType.HENTAI)
internal class Cat300(context: MangaLoaderContext) : MadaraParser(context, MangaParserSource.CAT_300, "cat-300.com") {
	override val datePattern = "MMMM dd, yyyy"
}
