package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MILFTOON", "MilfToon", "en", ContentType.HENTAI)
internal class MilfToon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MILFTOON, "milftoon.xxx", 20) {
	override val postReq = true
	override val datePattern = "d MMMM, yyyy"
}
