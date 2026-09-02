package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHWACLAN", "ManhwaClan", "en")
internal class ManhwaClan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHWACLAN, "manhwaclan.com", pageSize = 10) {
	override val datePattern = "MMMM dd, yyyy"
}
