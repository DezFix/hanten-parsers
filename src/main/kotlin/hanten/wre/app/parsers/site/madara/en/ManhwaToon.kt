package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWATOON", "ManhwaToon", "en", ContentType.HENTAI)
internal class ManhwaToon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWATOON, "www.manhwatoon.com")
