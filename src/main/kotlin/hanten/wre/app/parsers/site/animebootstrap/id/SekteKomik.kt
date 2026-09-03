package hanten.wre.app.parsers.site.animebootstrap.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.animebootstrap.AnimeBootstrapParser

@MangaSourceParser("SEKTEKOMIK", "SekteKomik", "id")
internal class SekteKomik(context: MangaLoaderContext) :
	AnimeBootstrapParser(context, MangaParserSource.SEKTEKOMIK, "sektekomik.id")
