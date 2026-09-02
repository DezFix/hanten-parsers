package hanten.wre.app.parsers.site.zeistmanga.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser
import hanten.wre.app.parsers.Broken

@Broken("Original site closed")
@MangaSourceParser("ARABSDOUJIN", "ArabsDoujin", "ar", ContentType.HENTAI)
internal class ArabsDoujin(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.ARABSDOUJIN, "www.arabsdoujin.online")
