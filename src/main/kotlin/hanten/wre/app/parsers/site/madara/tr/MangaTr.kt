package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGATR", "MangaTr", "tr")
internal class MangaTr(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGATR, "mangatr.app") {
	override val tagPrefix = "tur/"
}
