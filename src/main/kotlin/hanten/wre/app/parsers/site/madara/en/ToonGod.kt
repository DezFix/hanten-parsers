package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TOONGOD", "ToonGod", "en", ContentType.HENTAI)
internal class ToonGod(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TOONGOD, "www.toongod.org", 18) {
	override val listUrl = "webtoon/"
	override val tagPrefix = "webtoon-genre/"
	override val datePattern = "d MMM yyyy"
	override val withoutAjax = true
}
