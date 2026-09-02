package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@Broken("Redirect to @XMANHWA")
@MangaSourceParser("INSTAMANHWA", "InstaManhwa", "en", ContentType.HENTAI)
internal class InstaManhwa(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.INSTAMANHWA, "www.manhwaden.com", 15) {
	override val sourceLocale: Locale = Locale.ENGLISH
	override val selectPage = "img"
}
