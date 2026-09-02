package hanten.wre.app.parsers.site.mmrcms.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mmrcms.MmrcmsParser

@MangaSourceParser("BANANASCAN_COM", "BananaScan.Com", "en")
internal class BananaScan(context: MangaLoaderContext) :
	MmrcmsParser(context, MangaParserSource.BANANASCAN_COM, "bananascans.com")
