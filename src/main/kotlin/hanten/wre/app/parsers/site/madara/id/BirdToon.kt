package hanten.wre.app.parsers.site.madara.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@MangaSourceParser("BIRDTOON", "BirdToon", "id", ContentType.HENTAI)
internal class BirdToon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BIRDTOON, "birdtoon.shop", 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
	override val tagPrefix = "komik-genre/"
	override val listUrl = "komik/"
}
