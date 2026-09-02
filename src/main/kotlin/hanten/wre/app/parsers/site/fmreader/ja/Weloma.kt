package hanten.wre.app.parsers.site.fmreader.ja

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.fmreader.FmreaderParser

@MangaSourceParser("WELOMA", "Weloma", "ja")
internal class Weloma(context: MangaLoaderContext) :
	FmreaderParser(context, MangaParserSource.WELOMA, "weloma.art")
