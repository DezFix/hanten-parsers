package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("CRYSTALSCAN", "CrystalComics", "pt")
internal class CrystalScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.CRYSTALSCAN, "crystalcomics.com")

