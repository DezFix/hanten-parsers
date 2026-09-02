package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ILLUSIONSCAN", "IllusionScan", "pt", ContentType.HENTAI)
internal class IllusionScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ILLUSIONSCAN, "illusionscan.com") {
	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
