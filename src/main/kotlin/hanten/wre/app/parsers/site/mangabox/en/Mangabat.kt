package hanten.wre.app.parsers.site.mangabox.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangabox.MangaboxParser

@MangaSourceParser("HMANGABAT", "MangaBat", "en")
internal class Mangabat(context: MangaLoaderContext) :
	MangaboxParser(context, MangaParserSource.HMANGABAT) {
	override val configKeyDomain = ConfigKey.Domain("mangabats.com")
	override val selectTagMap = "div.panel-category p.pn-category-row:not(.pn-category-row-border) a"
}
