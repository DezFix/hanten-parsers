package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGANANQUIM", "MangaNanquim", "pt")
internal class MangaNanquim(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGANANQUIM, "mangananquim.site", 10) {
	override val datePattern: String = "d 'de' MMMM 'de' yyyy"
	override val listUrl = "ler-manga/"
	override val tagPrefix = "manga-genero/"
}
