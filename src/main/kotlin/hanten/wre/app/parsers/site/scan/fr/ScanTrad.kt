package hanten.wre.app.parsers.site.scan.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.scan.ScanParser

@MangaSourceParser("SCANTRAD", "ScanTrad", "fr")
internal class ScanTrad(context: MangaLoaderContext) :
	ScanParser(context, MangaParserSource.SCANTRAD, "scan-trad.com")
