package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("DECCALSCANS", "DeccalScans", "tr", ContentType.HENTAI)
internal class DeccalScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DECCALSCANS, "fuchscans.com") {
	override val tagPrefix = "turler/"
}