package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("SCANHENTAI", "ScanHentai", "fr", ContentType.HENTAI)
internal class ScanHentai(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SCANHENTAI, "scan-hentai.fr") {
	override val datePattern = "dd/MM/yyyy"
}
