package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANGAOWL_ONE", "MangaOwl.one", "en", ContentType.HENTAI)
internal class MangaowlOne(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAOWL_ONE, "mangaowl.one") {
	override val postReq = true
}
