package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWA18ORG", "Manhwa18.org", "en", ContentType.HENTAI)
internal class Manhwa18Org(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWA18ORG, "manhwa18.org") {
	override val postReq = true
}
