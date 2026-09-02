package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("YETISKINRUYAMANGA", "Yetişkin Rüya Manga", "tr")
internal class YetiskinRuyaManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YETISKINRUYAMANGA, "www.yetiskinruyamanga.com", 16) {
	override val datePattern = "dD/MM/yyyy"
}
