package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TRMANGAOKU", "TrMangaOku", "tr")
internal class TrMangaOku(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TRMANGAOKU, "trmangaoku.com") {
	override val tagPrefix = "tur/"
}
