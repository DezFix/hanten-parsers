package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.util.toAbsoluteUrl
import hanten.wre.app.parsers.util.toRelativeUrl

@MangaSourceParser("HIPERDEX", "HiperToon", "en", ContentType.HENTAI)
internal class HiperDex(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HIPERDEX, "hiperdex.com", 36) {

	override val listUrl = ""

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		// Remove ?style=list parameter from chapter URLs
		val absoluteUrl = chapter.url.toAbsoluteUrl(domain)
		val cleanUrl = if (absoluteUrl.contains("?style=list")) {
			absoluteUrl.replace("?style=list", "").replace("&style=list", "")
		} else {
			absoluteUrl
		}
		val relativeCleanUrl = cleanUrl.toRelativeUrl(domain)
		val modifiedChapter = chapter.copy(url = relativeCleanUrl)
		return super.getPages(modifiedChapter)
	}
}
