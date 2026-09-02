package hanten.wre.app.parsers.site.guya.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.guya.GuyaParser

@MangaSourceParser("DANKE", "DankeFursLesen", "en")
internal class Danke(context: MangaLoaderContext) :
	GuyaParser(context, MangaParserSource.DANKE, "danke.moe")
