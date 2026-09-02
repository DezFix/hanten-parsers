package hanten.wre.app.parsers.site.madara.id

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("WORLDMANHWAS", "WorldManhwas", "id", ContentType.HENTAI)
internal class WorldManhwas(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.WORLDMANHWAS, "worldmanhwas.zone", 10) {
	override val tagPrefix = "genre/"
	override val listUrl = "komik/"
	override val datePattern = "d MMMM yyyy"
}
