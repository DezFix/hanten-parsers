package hanten.wre.app.parsers.site.mangareader.en

import androidx.collection.ArrayMap
import kotlinx.coroutines.sync.withLock
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaTag
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.util.*

@Broken
@MangaSourceParser("FREAKCOMIC", "FreakComic", "en")
internal class FreakComic(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.FREAKCOMIC, "freakcomic.com", pageSize = 20, searchPageSize = 10) {

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
	override val selectMangaList = ".listupd .lastest-serie"
	override val selectMangaListImg = "img"
	override val selectChapter = ".chapter-li a:not(:has(svg))"
	override val detailsDescriptionSelector = "#summary"

	override suspend fun getOrCreateTagMap(): Map<String, MangaTag> = mutex.withLock {
		tagCache?.let { return@withLock it }
		val tagMap = ArrayMap<String, MangaTag>()
		val url = "https://$domain/genres/"
		val tagElements = webClient.httpGet(url).parseHtml().select("ul.genre-list > li.genre-item > a")
		for (el in tagElements) {
			if (el.text().isEmpty()) continue
			tagMap[el.text()] = MangaTag(
				title = el.text().toTitleCase(),
				key = el.attrAsAbsoluteUrl("href")
					.toHttpUrlOrNull()
					?.queryParameter("genre")
					?.nullIfEmpty()
					?: continue,
				source = source,
			)
		}
		tagCache = tagMap
		return@withLock tagMap
	}
}
