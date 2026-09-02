package hanten.wre.app.parsers.site.zmanga.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zmanga.ZMangaParser

@Broken
@MangaSourceParser("YURAMANGA", "YuraManga", "id")
internal class YuraManga(context: MangaLoaderContext) :
	ZMangaParser(context, MangaParserSource.YURAMANGA, "www.yuramanga.my.id")

