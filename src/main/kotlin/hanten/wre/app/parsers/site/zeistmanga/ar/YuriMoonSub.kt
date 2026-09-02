package hanten.wre.app.parsers.site.zeistmanga.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser
import hanten.wre.app.parsers.model.ContentType

@MangaSourceParser("YURIMOONSUB", "Yurimoonsub.blogspot.com", "ar", type = ContentType.HENTAI)
internal class YuriMoonSub(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.YURIMOONSUB, "yurimoonsub.blogspot.com")
