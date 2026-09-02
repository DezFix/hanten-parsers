package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("READER_EVILFLOWERS", "EvilFlowers", "en")
internal class ReaderEvilflowers(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.READER_EVILFLOWERS, "evilflowers.com", pageSize = 10) {
	override val listUrl = "project/"
}
