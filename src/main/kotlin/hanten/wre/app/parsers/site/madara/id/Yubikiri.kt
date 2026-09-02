package hanten.wre.app.parsers.site.madara.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("YUBIKIRI", "Yubikiri", "id")
internal class Yubikiri(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YUBIKIRI, "yubikiri.my.id", 18) {
	override val tagPrefix = "genre/"
	override val datePattern = "d MMMM"
}
