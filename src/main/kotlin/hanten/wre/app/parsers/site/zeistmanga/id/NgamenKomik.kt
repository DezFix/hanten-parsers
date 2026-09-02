package hanten.wre.app.parsers.site.zeistmanga.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@MangaSourceParser("NGAMENKOMIK", "NgamenKomik", "id")
internal class NgamenKomik(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.NGAMENKOMIK, "ngamenkomik05.blogspot.com")
