package hanten.wre.app.parsers.site.fmreader.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.site.fmreader.FmreaderParser
import hanten.wre.app.parsers.util.*

@MangaSourceParser("OLIMPOSCANS", "OlimpoScans", "es")
internal class OlimpoScans(context: MangaLoaderContext) :
	FmreaderParser(context, MangaParserSource.OLIMPOSCANS, "leerolimpo.com") {

	override val selectState = "ul.manga-info li:contains(Estado) a"
	override val selectAlt = "ul.manga-info li:contains(Otros nombres)"
	override val selectTag = "ul.manga-info li:contains(Género) a"
	override val tagPrefix = "lista-de-comics-genero-"

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = ("/" + chapter.url).toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		return doc.select(selectPage).map { img ->
			val url = ("/proxy.php?link=" + img.requireSrc()).toRelativeUrl(domain)
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}
}
