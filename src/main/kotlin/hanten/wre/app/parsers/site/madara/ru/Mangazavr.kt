package hanten.wre.app.parsers.site.madara.ru

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAZAVR", "Mangazavr", "ru", ContentType.HENTAI)
internal class Mangazavr(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAZAVR, "mangazavr.ru") {
	override val listUrl = "/?s=&post_type=wp-manga"
	override val datePattern = "dd.MM.yyyy"
}
