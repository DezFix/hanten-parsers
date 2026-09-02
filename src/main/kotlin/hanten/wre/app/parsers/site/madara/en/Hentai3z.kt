package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Redirect to @hentai20")
@MangaSourceParser("HENTAI3Z", "Hentai3z", "en", ContentType.HENTAI)
internal class Hentai3z(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HENTAI3Z, "manga18h.xyz", pageSize = 20) {
	override val withoutAjax = true
}
