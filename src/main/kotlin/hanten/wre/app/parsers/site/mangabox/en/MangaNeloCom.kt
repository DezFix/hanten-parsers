package hanten.wre.app.parsers.site.mangabox.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangabox.MangaboxParser
@Broken
@MangaSourceParser("MANGANELO_COM", "MangaNelo.com", "en")
internal class MangaNeloCom(context: MangaLoaderContext) :
	MangaboxParser(context, MangaParserSource.MANGANELO_COM) {
	override val configKeyDomain = ConfigKey.Domain("nelomanga.com", "m.manganelo.com", "chapmanganelo.com")
	override val otherDomain = "chapmanganelo.com"
}
