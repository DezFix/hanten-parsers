package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("MANHWANEW", "ManhwaNew", "en")
internal class ManhwaNew(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWANEW, "manhwanew.com") {
	override val datePattern = "MM/dd/yyyy"
}
