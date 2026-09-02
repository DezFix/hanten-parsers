package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("FIRSTKISSMANHUA", "FirstKissManhua", "en")
internal class FirstKissManhua(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.FIRSTKISSMANHUA, "1stkissmanhua.net", 20) {
	override val listUrl = "manhua/"
}
