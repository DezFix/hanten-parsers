package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANHWA18APP", "Manhwa18.app", "en", ContentType.HENTAI)
internal class Manhwa18App(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWA18APP, "manhwa18.app") {
	override val postReq = true
}
