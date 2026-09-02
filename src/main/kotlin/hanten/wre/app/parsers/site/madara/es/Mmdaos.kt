package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("MMDAOS", "Mmdaos", "es")
internal class Mmdaos(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MMDAOS, "mmdaos.com")
