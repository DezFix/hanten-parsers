package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MSYPUBLISHER", "MsyPublisher", "en")
internal class MsyPublisher(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MSYPUBLISHER, "msypublisher.com", 20) {
	override val listUrl = "manhua/"
	override val selectGenre = "manhua-genre/"
}
