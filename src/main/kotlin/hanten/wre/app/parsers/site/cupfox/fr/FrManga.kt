package hanten.wre.app.parsers.site.cupfox.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.cupfox.CupFoxParser

@Broken
@MangaSourceParser("FRMANGA", "FrManga", "fr")
internal class FrManga(context: MangaLoaderContext) :
	CupFoxParser(context, MangaParserSource.FRMANGA, "www.frmanga.com")
