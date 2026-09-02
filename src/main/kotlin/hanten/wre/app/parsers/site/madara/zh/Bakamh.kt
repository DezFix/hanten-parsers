package hanten.wre.app.parsers.site.madara.zh

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("BAKAMH", "Bakamh", "zh")
internal class Bakamh(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BAKAMH, "bakamh.com") {
	override val datePattern = "YYYY 年 M 月 d 日"
}
