package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("BOYS_LOVE", "BoysLove", "en", ContentType.HENTAI)
internal class BoysLove(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BOYS_LOVE, "boyslove.me", 20) {
	override val tagPrefix = "boyslove-genre/"
	override val listUrl = "boyslove/"
	override val postReq = true
}
