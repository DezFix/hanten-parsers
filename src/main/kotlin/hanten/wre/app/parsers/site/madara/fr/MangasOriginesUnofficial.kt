package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken ("Not dead, changed template")
@MangaSourceParser("MANGASORIGINESUNOFFICIAL", "CrunchyScan", "fr")
internal class MangasOriginesUnofficial(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGASORIGINESUNOFFICIAL, "crunchyscan.fr")
