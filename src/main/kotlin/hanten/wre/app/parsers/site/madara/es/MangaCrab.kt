package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGA_CRAB", "MangaCrab", "es")
internal class MangaCrab(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGA_CRAB, "mangacrab.org") {
	override val datePattern = "dd/MM/yyyy"
	override val tagPrefix = "manga-genero/"
	override val listUrl = "series/"
	override val selectChapter = "div.listing-chapters_wrap > ul > li"
	override val selectDesc = "div.c-page__content div.modal-contenido p"
	override val selectState = "div.summary-content2"
}
