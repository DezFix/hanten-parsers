package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("KAKUSEIPROJECT", "KakuseiProject", "pt")
internal class KakuseiProject(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.KAKUSEIPROJECT, "kakuseiproject.com", 10)
