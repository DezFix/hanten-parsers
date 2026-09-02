package hanten.wre.app.parsers.site.zeistmanga.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser

@Broken
@MangaSourceParser("MAXGSSCAN", "MaxgsScan", "pt")
internal class MaxgsScan(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.MAXGSSCAN, "www.maxgsscan.online")
