package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("YAOIFLIX", "YaoiFlix", "tr", ContentType.HENTAI)
internal class YaoiFlix(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YAOIFLIX, "yaoiflix.me", 16)
