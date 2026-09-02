package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("JELLYRING", "Jellyring", "tr")
internal class Jellyring(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.JELLYRING, "jellyring.co")
