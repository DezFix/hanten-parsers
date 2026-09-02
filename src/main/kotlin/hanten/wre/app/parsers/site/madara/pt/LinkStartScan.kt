package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("LINKSTARTSCAN", "LinkStartScan", "pt")
internal class LinkStartScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.LINKSTARTSCAN, "www.linkstartscan.xyz") {
	override val datePattern: String = "dd/MM/yyyy"
}
