package hanten.wre.app.parsers.site.mangaworld.it

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangaworld.MangaWorldParser

@MangaSourceParser("MANGAWORLD", "MangaWorld", "it")
internal class MangaWorld(
	context: MangaLoaderContext,
) : MangaWorldParser(context, MangaParserSource.MANGAWORLD, "mangaworld.ac")
