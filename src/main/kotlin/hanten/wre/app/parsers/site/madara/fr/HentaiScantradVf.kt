package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("HENTAISCANTRADVF", "Hentai-Scantrad", "fr", ContentType.HENTAI)
internal class HentaiScantradVf(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HENTAISCANTRADVF, "hentai.scantrad-vf.cc") {
	override val datePattern = "d MMMM, yyyy"
}
