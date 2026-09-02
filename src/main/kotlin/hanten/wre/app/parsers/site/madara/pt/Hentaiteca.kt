package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("HENTAITECA", "Hentaiteca", "pt", ContentType.HENTAI)
internal class Hentaiteca(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HENTAITECA, "hentaiteca.net", pageSize = 10) {
	override val datePattern = "MM/dd/yyyy"
	override val tagPrefix = "genero/"
}
