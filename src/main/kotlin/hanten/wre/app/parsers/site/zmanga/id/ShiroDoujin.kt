package hanten.wre.app.parsers.site.zmanga.id

import org.jsoup.nodes.Document
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.zmanga.ZMangaParser
import hanten.wre.app.parsers.util.attrAsRelativeUrl
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.mapChapters
import hanten.wre.app.parsers.util.selectFirstOrThrow
import java.text.SimpleDateFormat

@MangaSourceParser("SHIRO_DOUJIN", "ShiroDoujin", "id", ContentType.HENTAI)
internal class ShiroDoujin(context: MangaLoaderContext) :
	ZMangaParser(context, MangaParserSource.SHIRO_DOUJIN, "shirodoujin.com") {

	override suspend fun getChapters(doc: Document): List<MangaChapter> {
		val dateFormat = SimpleDateFormat(datePattern, sourceLocale)
		return doc.body().select(selectChapter).mapChapters(reversed = true) { i, li ->
			val a = li.selectFirstOrThrow("a")
			val href = a.attrAsRelativeUrl("href")
			val dateText = li.selectFirst(selectDate)?.text()
			val numChapter = li.selectFirstOrThrow(".flexch-infoz span").html().substringAfterLast("Chapter ")
				.substringBefore("<span")
			MangaChapter(
				id = generateUid(href),
				title = null,
				number = i + 1f,
				volume = 0,
				url = href,
				uploadDate = parseChapterDate(
					dateFormat,
					dateText,
				),
				source = source,
				scanlator = null,
				branch = null,
			)
		}
	}
}
