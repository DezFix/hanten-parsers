package hanten.wre.app.parsers.site.ru.multichan

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource

@MangaSourceParser("MANGACHAN", "Манга-тян", "ru")
internal class MangaChanParser(context: MangaLoaderContext) : ChanParser(context, MangaParserSource.MANGACHAN) {
	// The site is migrating to the im. subdomain: the root page redirects there
	// and all manga links point to im.manga-chan.me
	override val configKeyDomain = ConfigKey.Domain("im.manga-chan.me", "manga-chan.me")
}
