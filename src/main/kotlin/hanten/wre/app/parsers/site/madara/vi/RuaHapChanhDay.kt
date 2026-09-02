package hanten.wre.app.parsers.site.madara.vi

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("RUAHAPCHANHDAY", "Rùa Hấp Chanh Dây", "vi")
internal class RuaHapChanhDay(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.RUAHAPCHANHDAY, "ruahapchanhday.com", 30) {
	override val datePattern = "dd/MM/yyyy"
}
