package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("FOXWHITE", "FoxWhite", "pt")
internal class FoxWhite(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.FOXWHITE, "foxwhite.com.br")
