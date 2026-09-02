package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Blocked by Cloudflare")
@MangaSourceParser("EPSILONSOFT", "EpsilonSoft", "fr")
internal class EpsilonSoft(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.EPSILONSOFT, "epsilonsoft.to") {
	override val datePattern = "dd/MM/yy"
	override val withoutAjax = true
}
