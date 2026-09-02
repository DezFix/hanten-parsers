package hanten.wre.app.parsers.site.keyoapp.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.keyoapp.KeyoappParser

@Broken
@MangaSourceParser("LAIDBACKSCANS", "LaidBackScans", "en")
internal class LaidBackScans(context: MangaLoaderContext) :
	KeyoappParser(context, MangaParserSource.LAIDBACKSCANS, "laidbackscans.org")
