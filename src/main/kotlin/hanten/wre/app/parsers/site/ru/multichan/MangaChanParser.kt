package hanten.wre.app.parsers.site.ru.multichan

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource

@MangaSourceParser("MANGACHAN", "Манга-тян", "ru")
internal class MangaChanParser(context: MangaLoaderContext) : ChanParser(context, MangaParserSource.MANGACHAN) {
	override val configKeyDomain = ConfigKey.Domain("manga-chan.me")
}
