package hanten.wre.app.parsers.site.zeistmanga.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@Broken
@MangaSourceParser("ASUPANKOMIK", "AsupanKomik", "id")
internal class AsupanKomik(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.ASUPANKOMIK, "www.asupankomik.my.id")
