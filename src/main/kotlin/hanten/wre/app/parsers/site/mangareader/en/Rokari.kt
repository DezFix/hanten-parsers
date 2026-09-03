package hanten.wre.app.parsers.site.mangareader.en

import okhttp3.Headers
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.util.attrAsAbsoluteUrlOrNull
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.toAbsoluteUrl
import java.util.*

@MangaSourceParser("ROKARICOMICS", "Rokari Comics", "en")
internal class RokariComics(context: MangaLoaderContext) :
	MangaReaderParser(
		context = context,
		source = MangaParserSource.ROKARICOMICS,
		domain = "rokaricomics.com",
		pageSize = 20,
		searchPageSize = 10,
	) {

	override val sourceLocale: Locale = Locale.ENGLISH
	override val selectChapter = "#chapterlist li:has(div.chbox):has(div.eph-num):has(a[href]):not(:has(.text-gold))"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)

	override fun getRequestHeaders(): Headers = super.getRequestHeaders().newBuilder()
		.add("Referer", "https://$domain/")
		.build()

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val chapterUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(chapterUrl).parseHtml()
		val known = HashSet<String>()
		val pages = ArrayList<MangaPage>()

		doc.select("#readerarea img[src], #readerarea img[data-src], #readerarea img[data-lazy-src]").forEach { img ->
			val url = img.attrAsAbsoluteUrlOrNull("data-lazy-src")
				?: img.attrAsAbsoluteUrlOrNull("data-src")
				?: img.attrAsAbsoluteUrlOrNull("src")
				?: return@forEach
			if (known.add(url)) {
				pages += MangaPage(
					id = generateUid(url),
					url = url,
					preview = null,
					source = source,
				)
			}
		}

		return pages.ifEmpty {
			super.getPages(chapter)
		}
	}

}
