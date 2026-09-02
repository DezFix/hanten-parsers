package hanten.wre.app.parsers.site.madtheme.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madtheme.MadthemeParser

@MangaSourceParser("BEEHENTAI", "BeeHentai", "en", ContentType.HENTAI)
internal class BeeHentai(context: MangaLoaderContext) :
	MadthemeParser(context, MangaParserSource.BEEHENTAI, "beehentai.com") {
	override val selectDesc = "div.section-body"
}
