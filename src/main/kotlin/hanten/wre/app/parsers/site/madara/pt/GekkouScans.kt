package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@Broken
@MangaSourceParser("GEKKOUSCANS", "GekkouScans", "pt", ContentType.HENTAI)
internal class GekkouScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.GEKKOUSCANS, "gekkou.site") {
	override val sourceLocale: Locale = Locale.ENGLISH
}
