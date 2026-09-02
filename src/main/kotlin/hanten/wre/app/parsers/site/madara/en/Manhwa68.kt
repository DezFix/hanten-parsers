package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWA68", "Manhwa68", "en", ContentType.HENTAI)
internal class Manhwa68(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWA68, "manhwa68.com")
