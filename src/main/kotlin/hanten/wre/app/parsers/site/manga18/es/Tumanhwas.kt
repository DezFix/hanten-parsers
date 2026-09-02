package hanten.wre.app.parsers.site.manga18.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.manga18.Manga18Parser

@MangaSourceParser("TUMANHWAS", "Tumanhwas", "es", ContentType.HENTAI)
internal class Tumanhwas(context: MangaLoaderContext) :
	Manga18Parser(context, MangaParserSource.TUMANHWAS, "tumanhwas.club") {
	override val selectTag = "div.item:contains(Géneros) div.info_value a"
	override val selectAlt = "div.item:contains(Títulos alternativos) div.info_value"
}
