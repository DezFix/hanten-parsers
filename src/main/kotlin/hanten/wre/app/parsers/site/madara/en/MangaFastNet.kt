package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAFASTNET", "MangaFast.net", "en")
internal class MangaFastNet(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAFASTNET, "manhuafast.net") {
	override val withoutAjax = true

}
