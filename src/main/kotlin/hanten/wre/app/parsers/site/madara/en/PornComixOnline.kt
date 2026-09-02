package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("PORNCOMIXONLINE", "PornComix.online", "en", ContentType.HENTAI)
internal class PornComixOnline(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PORNCOMIXONLINE, "porncomix.online") {
	override val listUrl = "comic/"
	override val tagPrefix = "comic-genre/"
	override val stylePage = ""
}
