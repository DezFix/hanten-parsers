package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("LICHMANGAS", "LichMangas", "pt")
internal class LichMangas(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LICHMANGAS, "lichmangas.com", 10) {
	override val datePattern = "dd/MM/yyyy"
}
