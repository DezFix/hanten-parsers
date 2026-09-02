package hanten.wre.app.parsers.site.scan.it

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.scan.ScanParser

@MangaSourceParser("MANGAITALIA", "MangaItalia", "it")
internal class MangaItalia(context: MangaLoaderContext) :
	ScanParser(context, MangaParserSource.MANGAITALIA, "mangaita.io")
