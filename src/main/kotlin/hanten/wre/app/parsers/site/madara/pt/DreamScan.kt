package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("DREAMSCAN", "DreamScan", "pt")
internal class DreamScan(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.DREAMSCAN, "fairydream.com.br")
