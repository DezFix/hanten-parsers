package hanten.wre.app.parsers.site.madara.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.util.insertCookies

@MangaSourceParser("HHENTAIFR", "H-Hentai", "fr", ContentType.HENTAI)
internal class HhentaiFr(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HHENTAIFR, "hhentai.fr") {

	init {
		context.cookieJar.insertCookies(
			domain,
			"age_gate=32;",
		)
	}
}
