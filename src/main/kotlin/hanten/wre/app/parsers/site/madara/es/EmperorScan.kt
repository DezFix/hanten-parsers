package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("EMPERORSCAN", "EmperorScan", "es")
internal class EmperorScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.EMPERORSCAN, "imperiomanhua.com") {

	override val withoutAjax = true

	override val datePattern = "MMMM dd, yyyy"

	override val selectDesc = "div.summary_content div.post-content_item:has(h5:contains(Sinopsis)) div"
}
