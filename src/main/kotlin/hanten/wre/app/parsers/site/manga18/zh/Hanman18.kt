package hanten.wre.app.parsers.site.manga18.zh

import org.jsoup.nodes.Document
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilterOptions
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.manga18.Manga18Parser
import hanten.wre.app.parsers.util.attrAsRelativeUrl
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.mapChapters
import hanten.wre.app.parsers.util.selectFirstOrThrow

@MangaSourceParser("HANMAN18", "Hanman18", "zh", ContentType.HENTAI)
internal class Hanman18(context: MangaLoaderContext) :
	Manga18Parser(context, MangaParserSource.HANMAN18, "hanman18.com") {

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = emptySet(),
	)

	override suspend fun getChapters(doc: Document): List<MangaChapter> {
		return doc.body().select(selectChapter).mapChapters(reversed = true) { i, li ->
			val a = li.selectFirstOrThrow("a")
			val href = a.attrAsRelativeUrl("href")
			MangaChapter(
				id = generateUid(href),
				title = a.text(),
				number = i + 1f,
				volume = 0,
				url = href,
				uploadDate = 0,
				source = source,
				scanlator = null,
				branch = null,
			)
		}
	}
}
