package hanten.wre.app.parsers.site.scan.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.scan.ScanParser

@Broken
@MangaSourceParser("MANGABR", "MangaBr", "pt")
internal class MangaBr(context: MangaLoaderContext) :
	ScanParser(context, MangaParserSource.MANGABR, "mangabr.net")
