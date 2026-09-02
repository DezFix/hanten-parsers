package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("DRAGONTRANSLATIONORG", "DragonTranslation.org", "es")
internal class DragonTranslationOrg(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DRAGONTRANSLATIONORG, "dragontranslation.org", 16) {
	override val datePattern = "dd/MM/yyyy"
}
