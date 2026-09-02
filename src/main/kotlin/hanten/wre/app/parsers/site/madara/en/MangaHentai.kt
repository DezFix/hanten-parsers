package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGAHENTAI", "MangaHentai", "en", ContentType.HENTAI)
internal class MangaHentai(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAHENTAI, "mangahentai.me", 20) {

	override val tagPrefix = "manga-hentai-genre/"
	override val listUrl = "manga-hentai/"
}
