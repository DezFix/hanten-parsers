package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.util.*

@MangaSourceParser("MANHUAPLUS", "ManhuaPlus", "en")
internal class Manhuaplus(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHUAPLUS, "manhuaplus.com") {

	override val withoutAjax = true

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		val root = doc.body().selectFirst("div.main-col-inner")?.selectFirst("div.reading-content")
			?: throw ParseException("Root not found", fullUrl)
		return root.select("img").map { img ->
			val url = img.requireSrc().toRelativeUrl(domain)
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}
}
