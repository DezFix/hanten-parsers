package hanten.wre.app.parsers.site.madara.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.Locale

@MangaSourceParser("XMANHWA", "XManhwa", "id", ContentType.HENTAI)
internal class XManhwa(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.XMANHWA, "www.manhwaden.com", 10) {
	override val sourceLocale: Locale = Locale.ENGLISH
	override val selectPage = "img"
}
