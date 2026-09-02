package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Redirect to @MortalsGroove")
@MangaSourceParser("IMMORTALUPDATES", "ImmortalUpdates", "en")
internal class ImmortalUpdates(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.IMMORTALUPDATES, "immortalupdates.com") {
	override val listUrl = "mangas/"
}
