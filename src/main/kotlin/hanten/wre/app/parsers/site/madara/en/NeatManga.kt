package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("NEATMANGA", "NeatManga", "en")
internal class NeatManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.NEATMANGA, "neatmangas.com", pageSize = 20) {
	override val datePattern = "dd MMMM yyyy"
}
