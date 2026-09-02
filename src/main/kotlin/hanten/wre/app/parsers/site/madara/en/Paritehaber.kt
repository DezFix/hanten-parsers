package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("PARITEHABER", "Paritehaber", "en", ContentType.HENTAI)
internal class Paritehaber(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PARITEHABER, "www.paritehaber.com", 10)
