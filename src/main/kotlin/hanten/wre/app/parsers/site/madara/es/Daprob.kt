package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("DAPROB", "Daprob", "es")
internal class Daprob(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DAPROB, "daprob.com") {
	override val datePattern = "d 'de' MMMMM 'de' yyyy"
}
