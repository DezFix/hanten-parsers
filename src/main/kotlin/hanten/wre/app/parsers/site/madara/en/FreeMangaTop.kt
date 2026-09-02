package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("FREEMANGATOP", "FreeMangaTop", "en")
internal class FreeMangaTop(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.FREEMANGATOP, "freemangatop.com") {
	override val datePattern = "MM/dd/yyyy"

    override fun getRequestHeaders() = super.getRequestHeaders().newBuilder()
        .add("Referer", "https://$domain/")
        .build()
}
