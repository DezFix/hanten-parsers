package hanten.wre.app.parsers.site.comicaso.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.comicaso.ComicasoParser

@MangaSourceParser("MEDUSASCANS", "Medusascans", "id", ContentType.HENTAI)
internal class Medusascans(context: MangaLoaderContext) :
	ComicasoParser(context, MangaParserSource.MEDUSASCANS, "medusascans.pro", pageSize = 16)
