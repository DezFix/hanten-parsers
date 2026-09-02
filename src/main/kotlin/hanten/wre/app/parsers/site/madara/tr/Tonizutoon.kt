package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("TONIZUTOON", "ToniZu.com", "tr", ContentType.HENTAI)
internal class Tonizutoon(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TONIZUTOON, "tonizu.top") {
	override val datePattern = "dd/mm/yyyy"
}
