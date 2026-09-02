package hanten.wre.app.parsers.site.madara.ar

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("SHADOWXMANGA", "ShadowXManga", "ar")
internal class ShadowxManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SHADOWXMANGA, "www.shadowxmanga.com") {
	override val datePattern = "yyyy/MM/dd"
}
