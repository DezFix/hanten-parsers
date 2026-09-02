package hanten.wre.app.parsers.site.keyoapp.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.keyoapp.KeyoappParser

@MangaSourceParser("REZOSCANS", "RezoScans", "en")
internal class RezoScans(context: MangaLoaderContext) :
	KeyoappParser(context, MangaParserSource.REZOSCANS, "rezoscans.com")
