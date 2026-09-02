package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("NOBLESSETRANSLATIONS", "NoblesseTranslations", "es")
internal class NoblesseTranslations(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.NOBLESSETRANSLATIONS, "nobledicion.yoveo.xyz")
