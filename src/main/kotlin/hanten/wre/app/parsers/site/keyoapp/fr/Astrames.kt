package hanten.wre.app.parsers.site.keyoapp.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.keyoapp.KeyoappParser

@Broken
@MangaSourceParser("ASTRAMES", "Astrames", "fr")
internal class Astrames(context: MangaLoaderContext) :
	KeyoappParser(context, MangaParserSource.ASTRAMES, "astrames.fr")
