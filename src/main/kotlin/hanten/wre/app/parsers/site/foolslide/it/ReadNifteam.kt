package hanten.wre.app.parsers.site.foolslide.it

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.foolslide.FoolSlideParser

@MangaSourceParser("READNIFTEAM", "ReadNifTeam", "it")
internal class ReadNifteam(context: MangaLoaderContext) :
	FoolSlideParser(context, MangaParserSource.READNIFTEAM, "read-nifteam.info") {
	override val searchUrl = "slide/search/"
	override val listUrl = "slide/directory/"
}
