package hanten.wre.app.parsers.site.onemanga.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.onemanga.OneMangaParser

@MangaSourceParser("CHAINSAWMANSCAN", "ChainsawManScan", "fr")
internal class ChainsawManScan(context: MangaLoaderContext) :
	OneMangaParser(context, MangaParserSource.CHAINSAWMANSCAN, "chainsawman-scan.com")
