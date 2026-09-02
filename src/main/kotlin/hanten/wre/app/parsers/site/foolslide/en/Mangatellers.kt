package hanten.wre.app.parsers.site.foolslide.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.foolslide.FoolSlideParser

@MangaSourceParser("MANGATELLERS", "Mangatellers", "en")
internal class Mangatellers(context: MangaLoaderContext) :
	FoolSlideParser(context, MangaParserSource.MANGATELLERS, "reader.mangatellers.gr") {
	override val pagination = false
}
