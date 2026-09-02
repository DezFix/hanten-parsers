package hanten.wre.app.parsers.site.zeistmanga.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@MangaSourceParser("LEPOYTL", "Lepoytl", "id")
internal class Lepoytl(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.LEPOYTL, "www.lepoytl.cloud")
