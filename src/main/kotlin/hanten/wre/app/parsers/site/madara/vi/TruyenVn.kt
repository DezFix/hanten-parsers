package hanten.wre.app.parsers.site.madara.vi

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TRUYENVN", "TruyenVn", "vi", ContentType.HENTAI)
internal class TruyenVn(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TRUYENVN, "truyenvn.shop", 20) {
	override val listUrl = "truyen-tranh/"
	override val tagPrefix = "the-loai/"
	override val datePattern = "dd/MM/yyyy"
}
