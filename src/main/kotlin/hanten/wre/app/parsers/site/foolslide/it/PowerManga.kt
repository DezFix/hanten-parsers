package hanten.wre.app.parsers.site.foolslide.it

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.foolslide.FoolSlideParser

@Broken
@MangaSourceParser("POWERMANGA", "PowerManga", "it")
internal class PowerManga(context: MangaLoaderContext) :
	FoolSlideParser(context, MangaParserSource.POWERMANGA, "reader.powermanga.org") {
	override val pagination = false
}
