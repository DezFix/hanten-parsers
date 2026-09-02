package hanten.wre.app.parsers.site.onemanga.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.onemanga.OneMangaParser

@MangaSourceParser("FIREFORCE", "FireForce", "fr")
internal class FireForce(context: MangaLoaderContext) :
	OneMangaParser(context, MangaParserSource.FIREFORCE, "fireforce.fr")
