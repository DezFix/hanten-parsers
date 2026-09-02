package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("PIEDPIPERFANSUBYY", "PiedPiperFansubyy", "tr", ContentType.HENTAI)
internal class PiedPiperFansubyy(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PIEDPIPERFANSUBYY, "piedpiperfansubyy.me", 18) {
	override val datePattern = "d MMMM yyyy"
}
