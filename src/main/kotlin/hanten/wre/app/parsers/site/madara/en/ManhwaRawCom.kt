package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWARAW_COM", "ManhwaRaw.com", "en", ContentType.HENTAI)
internal class ManhwaRawCom(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWARAW_COM, "manhwaraw.com") {
	override val postReq = true
	override val listUrl = "manhwa-raw/"
	override val tagPrefix = "manhwa-raw-genre/"
}
