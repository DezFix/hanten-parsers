package hanten.wre.app.parsers.site.madara.ru

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAONELOVE", "MangaOneLove", "ru")
internal class MangaoneLove(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAONELOVE, "mangaonelove.space", 10) {
	override val configKeyDomain =
		ConfigKey.Domain("mangaonelove.space", "mangaonelove.website", "mangaonelove.site")
	override val datePattern = "dd.MM.yyyy"
	override val postReq = true
}
