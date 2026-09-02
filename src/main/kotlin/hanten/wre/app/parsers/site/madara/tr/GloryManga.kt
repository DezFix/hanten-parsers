package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Redirect to @MANGAGEZGINI")
@MangaSourceParser("GLORYMANGA", "GloryManga", "tr")
internal class GloryManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.GLORYMANGA, "mangagezgini.site", 18) {
	override val datePattern = "dd/MM/yyyy"
}
