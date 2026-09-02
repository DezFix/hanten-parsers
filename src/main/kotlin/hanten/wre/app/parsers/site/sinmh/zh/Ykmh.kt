package hanten.wre.app.parsers.site.sinmh.zh

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.sinmh.SinmhParser

@MangaSourceParser("YKMH", "Ykmh", "zh")
internal class Ykmh(context: MangaLoaderContext) :
	SinmhParser(context, MangaParserSource.YKMH, "www.ykmh.net")
