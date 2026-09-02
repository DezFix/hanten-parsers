package hanten.wre.app.parsers.site.animebootstrap.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.animebootstrap.AnimeBootstrapParser

@MangaSourceParser("KOMIKZOID", "KomikzoId", "id")
internal class KomikzoId(context: MangaLoaderContext) :
	AnimeBootstrapParser(context, MangaParserSource.KOMIKZOID, "komikzoid.id")
