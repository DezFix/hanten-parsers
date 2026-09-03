package hanten.wre.app.parsers.site.zeistmanga.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@Broken
@MangaSourceParser("NIMEMOB", "Nimemob", "id")
internal class Nimemob(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.NIMEMOB, "www.nimemob.my.id")
