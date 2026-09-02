package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("YAOITR", "YaoiTr", "tr")
internal class YaoiTr(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YAOITR, "yaoitr.fun", 16) {
	override val datePattern = "d MMMM yyyy"
}
