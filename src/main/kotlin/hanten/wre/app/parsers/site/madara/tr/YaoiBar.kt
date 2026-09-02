package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("YAOIBAR", "YaoiBar", "tr")
internal class YaoiBar(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YAOIBAR, "yaoibar.gay", 16) {
	override val datePattern = "dd/MM/yyyy"
}
