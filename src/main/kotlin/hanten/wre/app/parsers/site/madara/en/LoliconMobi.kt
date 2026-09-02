package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("LOLICONMOBI", "LoliconMobi", "en", ContentType.HENTAI)
internal class LoliconMobi(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LOLICONMOBI, "lolicon.mobi") {
	override val postReq = true
	override val tagPrefix = "lolicon-genre/"
}
