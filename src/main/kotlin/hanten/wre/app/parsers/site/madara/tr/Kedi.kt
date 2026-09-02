package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("KEDI", "Kedi", "tr")
internal class Kedi(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.KEDI, "kedi.to") {
	override val datePattern = "d MMMM yyyy"
	override val tagPrefix = "tur/"
}
