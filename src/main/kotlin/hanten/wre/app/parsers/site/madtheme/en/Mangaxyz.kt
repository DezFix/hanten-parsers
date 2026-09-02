package hanten.wre.app.parsers.site.madtheme.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madtheme.MadthemeParser
import hanten.wre.app.parsers.util.toAbsoluteUrl
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.parseHtml

@MangaSourceParser("MANGAXYZ", "MangaXyz", "en")
internal class Mangaxyz(context: MangaLoaderContext) :
	MadthemeParser(context, MangaParserSource.MANGAXYZ, "mangaxyz.com") {

    private val subDomain = "sb.mbcdn.xyz"

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		val regexPages = Regex("chapImages\\s*=\\s*['\"](.*?)['\"]")
		val pages = doc.select("script").firstNotNullOfOrNull { script ->
			regexPages.find(script.html())?.groupValues?.getOrNull(1)
		}?.split(',')

		return pages?.map { url ->
			val cleanUrl = url.substringAfter("/manga")
			MangaPage(
				id = generateUid(url),
				url = "https://$subDomain/manga$cleanUrl",
				preview = null,
				source = source,
			)
		} ?: emptyList()
	}

}
