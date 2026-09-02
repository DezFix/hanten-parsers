package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("ANCIENTCOMICS", "AncientComics", "pt")
internal class AncientComics(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ANCIENTCOMICS, "ancientcomics.com.br") {
	override val datePattern: String = "dd/MM/yyyy"
	override val withoutAjax = true
}
