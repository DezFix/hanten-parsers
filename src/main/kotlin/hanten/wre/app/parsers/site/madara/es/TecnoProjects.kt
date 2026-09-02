package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("TECNOPROJECTS", "TecnoProjects", "es")
internal class TecnoProjects(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.TECNOPROJECTS, "tecnoprojects.xyz") {
	override val datePattern = "dd 'de' MMMM 'de' yyyy"
}
