package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("GEDECOMIX", "GedeComix", "en", ContentType.HENTAI)
internal class GedeComix(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.GEDECOMIX, "gedecomix.com", 18) {
	override val tagPrefix = "comics-tag/"
	override val listUrl = "porncomic/"
}
