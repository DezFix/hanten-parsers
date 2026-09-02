package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MARMOTA", "Marmota", "es", ContentType.COMICS)
internal class Marmota(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MARMOTA, "marmota.me", 48) {
	override val datePattern = "d 'de' MMMMM 'de' yyyy"
	override val tagPrefix = "genero/"
	override val listUrl = "comic/"
}
