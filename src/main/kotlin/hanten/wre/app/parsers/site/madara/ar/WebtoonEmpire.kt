package hanten.wre.app.parsers.site.madara.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.model.ContentType

@MangaSourceParser("WEBTOONEMPIRE", "WebtoonEmpire", "ar", type = ContentType.HENTAI)
internal class WebtoonEmpire(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.WEBTOONEMPIRE, "webtoonempire-bl.com", pageSize = 10) {
	override val listUrl = "webtoon/"
	override val datePattern = "d MMMM، yyyy"
	override val withoutAjax = true
}
