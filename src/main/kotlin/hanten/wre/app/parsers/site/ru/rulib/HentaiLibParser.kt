package hanten.wre.app.parsers.site.ru.rulib

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource

@Broken
@MangaSourceParser("HENTAILIB", "HentaiLib", "ru", type = ContentType.HENTAI)
internal class HentaiLibParser(context: MangaLoaderContext) : LibSocialParser(
	context = context,
	source = MangaParserSource.HENTAILIB,
	siteId = 4,
	siteDomains = arrayOf("v1.hentailib.org", "hentailib.me"),
)
