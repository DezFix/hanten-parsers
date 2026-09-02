package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Redirect to @MANGAREAD")
@MangaSourceParser("MANGAEFFECT", "MangaEffect", "en")
internal class MangaEffect(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANGAEFFECT, "www.mangaread.org") {
	override val datePattern = "dd.MM.yyyy"
	override val withoutAjax = true
}
