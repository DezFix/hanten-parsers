package hanten.wre.app.parsers.site.mmrcms.en

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.Interceptor
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentRating
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.Manga
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaListFilterOptions
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaState
import hanten.wre.app.parsers.model.MangaTag
import hanten.wre.app.parsers.model.RATING_UNKNOWN
import hanten.wre.app.parsers.model.SortOrder
import hanten.wre.app.parsers.site.mmrcms.MmrcmsParser
import hanten.wre.app.parsers.util.attrAsRelativeUrl
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.mapChapters
import hanten.wre.app.parsers.util.nullIfEmpty
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.parseSafe
import hanten.wre.app.parsers.util.requireSrc
import hanten.wre.app.parsers.util.src
import hanten.wre.app.parsers.util.textOrNull
import hanten.wre.app.parsers.util.toAbsoluteUrl
import hanten.wre.app.parsers.util.toRelativeUrl
import java.text.SimpleDateFormat
import java.util.EnumSet
import java.util.Locale

@MangaSourceParser("READCOMICSONLINE", "ReadComicsOnline.ru", "en", ContentType.COMICS)
internal class ReadComicsOnline(context: MangaLoaderContext) :
	MmrcmsParser(context, MangaParserSource.READCOMICSONLINE, "readcomicsonline.ru") {

	private val chapterDateFormat = SimpleDateFormat("d MMM yyyy", Locale.US)

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.POPULARITY,
		SortOrder.UPDATED,
	)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities()

	override suspend fun getFilterOptions(): MangaListFilterOptions = MangaListFilterOptions()

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val newRequest = request.newBuilder()
			.header("Referer", "https://$domain/")
			.build()
		return chain.proceed(newRequest)
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		if (!filter.query.isNullOrEmpty() || filter.tags.isNotEmpty() || filter.states.isNotEmpty()) {
			return emptyList()
		}
		val sort = when (order) {
			SortOrder.UPDATED -> "latest"
			else -> "views"
		}
		val doc = webClient.httpGet("https://$domain/comic-list?sort=$sort&page=$page").parseHtml()
		return doc.select("div.comic-list-layout .grid > .group").mapNotNull(::parseMangaListItem)
	}

	private fun parseMangaListItem(element: Element): Manga? {
		val anchor = element.selectFirst("a.block.text-sm.font-semibold") ?: return null
		val href = anchor.attrAsRelativeUrl("href")
		return Manga(
			id = generateUid(href),
			title = anchor.text(),
			altTitles = emptySet(),
			url = href,
			publicUrl = href.toAbsoluteUrl(element.baseUriHost()),
			rating = RATING_UNKNOWN,
			contentRating = if (isNsfwSource) ContentRating.ADULT else null,
			coverUrl = guessCover(href, element.selectFirst("img")?.src()),
			tags = emptySet(),
			state = null,
			authors = emptySet(),
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga = coroutineScope {
		val fullUrl = manga.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		val chaptersDeferred = async { getChapters(doc, manga.title) }
		manga.copy(
			title = doc.selectFirst("h1.text-2xl")?.textOrNull() ?: manga.title,
			coverUrl = guessCover(manga.url, doc.selectFirst("img.w-full.rounded-xl")?.src()) ?: manga.coverUrl,
			description = doc.selectFirst("p.mt-5.text-sm")?.textOrNull(),
			state = parseState(doc.selectFirst("div.flex.flex-wrap.gap-2 span.rounded-full")?.text()),
			tags = doc.select("dl div:contains(Genres:) a").mapTo(LinkedHashSet()) {
				MangaTag(
					key = it.attr("href").removeSuffix("/").substringAfterLast('/'),
					title = it.text(),
					source = source,
				)
			},
			authors = doc.select("div:has(span:contains(Author:)) > a").mapTo(LinkedHashSet()) { it.text() },
			chapters = chaptersDeferred.await(),
		)
	}

	private fun parseState(value: String?): MangaState? {
		return when (value?.lowercase(Locale.US)) {
			"complete", "completed" -> MangaState.FINISHED
			"ongoing", "on going" -> MangaState.ONGOING
			"dropped", "cancelled", "canceled" -> MangaState.ABANDONED
			else -> null
		}
	}

	private fun getChapters(doc: Document, mangaTitle: String): List<MangaChapter> {
		return doc.select(".overflow-hidden.border-ink-600 > a").mapChapters(reversed = true) { i, element ->
			val href = element.attrAsRelativeUrl("href")
			val chapterName = element.selectFirst(".text-brand-400")?.textOrNull() ?: element.text()
			MangaChapter(
				id = generateUid(href),
				title = cleanChapterName(mangaTitle, chapterName),
				number = i + 1f,
				volume = 0,
				url = href,
				uploadDate = chapterDateFormat.parseSafe(element.selectFirst(".text-slate-500")?.text()),
				source = source,
				scanlator = null,
				branch = null,
			)
		}
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain)).parseHtml()
		return doc.select("#reader-all img").mapIndexed { i, img ->
			val url = img.requireSrc().toRelativeUrl(domain)
			MangaPage(
				id = generateUid(url),
				url = url,
				preview = null,
				source = source,
			)
		}
	}

	private fun cleanChapterName(mangaTitle: String, chapterName: String): String {
		return chapterName
			.removePrefix(mangaTitle)
			.trimStart(' ', '-', ':')
			.nullIfEmpty()
			?: chapterName
	}

	private fun guessCover(mangaUrl: String, imageUrl: String?): String? {
		imageUrl?.takeUnless { it.contains("/cover/cover_missing.", ignoreCase = true) }?.let {
			return it
		}
		val slug = mangaUrl.removeSuffix("/").substringAfterLast('/').nullIfEmpty() ?: return null
		return "https://$domain/uploads/manga/$slug/cover/cover_250x350.jpg"
	}

	private fun Element.baseUriHost(): String {
		return baseUri().substringAfter("://", domain).substringBefore('/').ifEmpty { domain }
	}
}
