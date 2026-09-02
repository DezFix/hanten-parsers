package hanten.wre.app.parsers.site.zeistmanga.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@MangaSourceParser("ELEVENSCANLATOR", "ElevenScanlator", "pt")
internal class ElevenScanlator(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.ELEVENSCANLATOR, "elevenscanlator.blogspot.com") {
	override val sateOngoing: String = "Lançando"
	override val sateFinished: String = "Completo"
	override val sateAbandoned: String = "Dropado"
}
