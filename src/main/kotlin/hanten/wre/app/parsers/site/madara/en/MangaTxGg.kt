package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANGATX_GG", "MangaTx.gg", "en")
internal class MangaTxGg(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGATX_GG, "mangatx.gg")
