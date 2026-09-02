package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("YAOIX3", "3XYaoi", "pt", ContentType.HENTAI)
internal class YaoiX3(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.YAOIX3, "3xyaoi.com") {
	override val datePattern = "dd/MM/yyyy"
	override val listUrl = "bl/"
	override val tagPrefix = "genero/"
}
