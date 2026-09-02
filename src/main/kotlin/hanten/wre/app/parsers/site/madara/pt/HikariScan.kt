package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("HIKARISCAN", "HikariScan", "pt")
internal class HikariScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HIKARISCAN, "hikariscan.org")
