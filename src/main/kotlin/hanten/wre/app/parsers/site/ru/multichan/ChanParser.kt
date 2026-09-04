package hanten.wre.app.parsers.site.ru.multichan

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.internal.StringUtil
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaParserAuthProvider
import hanten.wre.app.parsers.core.AbstractMangaParser
import hanten.wre.app.parsers.exception.AuthRequiredException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import java.text.SimpleDateFormat
import java.util.*

internal abstract class ChanParser(
	context: MangaLoaderContext,
	source: MangaParserSource,
) : AbstractMangaParser(context, source), MangaParserAuthProvider {

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.NEWEST,
		SortOrder.POPULARITY,
		SortOrder.ALPHABETICAL,
		SortOrder.RATING,
	)

	override val authUrl: String
		get() = "https://${domain}"

	override suspend fun isAuthorized(): Boolean = context.cookieJar.getCookies(domain).any { it.name == "dle_user_id" }

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isMultipleTagsSupported = true,
			isTagsExclusionSupported = true,
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = fetchAvailableTags(),
	)

	override suspend fun getList(offset: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val domain = domain
		val doc = webClient.httpGet(buildUrl(offset, order, filter)).parseHtml()
		val root = doc.body().selectFirst("div.main_fon")?.getElementById("content")
			?: doc.parseFailed("Cannot find root")
		return root.select("div.content_row").mapNotNull { row ->
			val a = row.selectFirst("div.manga_row1")?.selectFirst("h2")?.selectFirst("a")
				?: return@mapNotNull null
			val href = a.attrAsRelativeUrl("href")
			val title = a.text().parseTitle()
			val author = row.getElementsByAttributeValueStarting(
				"href",
				"/mangaka",
			).firstOrNull()?.text()
			Manga(
				id = generateUid(a.attrAsRelativeUrlAnyHost("href")),
				url = href,
				publicUrl = href.toAbsoluteUrl(a.host ?: domain),
				altTitles = setOfNotNull(title.second),
				title = title.first,
				authors = setOfNotNull(author),
				coverUrl = row.selectFirst("div.manga_images")?.selectFirst("img")
					?.absUrl("src").orEmpty(),
				tags = runCatching {
					row.selectFirst("div.genre")?.select("a")?.mapToSet {
						MangaTag(
							title = it.text().toTagName(),
							key = it.attr("href").substringAfterLast('/').urlDecode(),
							source = source,
						)
					}
				}.getOrNull().orEmpty(),
				rating = RATING_UNKNOWN,
				state = null,
				contentRating = if (isNsfwSource) ContentRating.ADULT else null,
				source = source,
			)
		}
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain)).parseHtml()
		// Fall back to the whole body: challenge/redirect shells have no dle-content,
		// crashing on them hides the real problem and breaks the CF retry flow.
		val root = doc.body().getElementById("dle-content") ?: doc.body()
		val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
		return manga.copy(
			description = root.getElementById("description")?.html()?.substringBeforeLast("<div"),
			largeCoverUrl = root.getElementById("cover")?.absUrl("src"),
			chapters = findChapterRows(root).mapChapters(reversed = true) { i, row ->
				parseChapterRow(row, i, dateFormat)
			},
		)
	}

	private fun findChapterRows(root: Element): List<Element> {
		root.select("table.table_cha tr:gt(1)").takeIf { it.isNotEmpty() }?.let { return it }
		// New im.manga-chan.me markup: tr.no_zaliv / tr.zaliv with div.manga2 links
		root.select("tr.no_zaliv, tr.zaliv").takeIf { it.isNotEmpty() }?.let { return it }
		return emptyList()
	}

	private fun parseChapterRow(row: Element, index: Int, dateFormat: SimpleDateFormat): MangaChapter? {
		val tr = row.selectFirstParent("tr") ?: row.takeIf { it.tagName() == "tr" } ?: return null
		val a = tr.selectFirst("div.manga2 a") ?: tr.selectFirst("a") ?: return null
		val href = a.attrAsRelativeUrlOrNull("href") ?: return null
		return MangaChapter(
			id = generateUid(a.attrAsRelativeUrlAnyHost("href")),
			title = a.textOrNull(),
			number = HREF_V_CH_REGEX.find(href)?.groupValues?.getOrNull(2)?.toFloatOrNull()
				?: (index + 1f),
			volume = tr.attr("data-vol").toIntOrNull()
				?: HREF_V_CH_REGEX.find(href)?.groupValues?.getOrNull(1)?.toIntOrNull()
				?: 0,
			url = href,
			scanlator = null,
			branch = null,
			uploadDate = dateFormat.parseSafe(tr.selectFirst("td.date, div.date")?.text()),
			source = source,
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val fullUrl = chapter.url.toAbsoluteUrl(domain)
		val doc = webClient.httpGet(fullUrl).parseHtml()
		parsePagesFullimg(doc)?.let { return it }
		val scripts = doc.select("script")
		for (script in scripts) {
			val data = script.html()
			val pos = data.indexOf("\"fullimg")
			if (pos == -1) {
				continue
			}
			val json = data.substring(pos).substringAfter('[').substringBefore(';')
				.substringBeforeLast(']')
			val domain = domain
			return json.split(",").mapNotNull {
				it.trim()
					.removeSurrounding('"', '\'')
					.toRelativeUrl(domain)
					.takeUnless(String::isBlank)
			}.map { url ->
				MangaPage(
					id = generateUid(url),
					url = url,
					preview = null,
					source = source,
				)
			}
		}
		doc.parseFailed("Pages list not found at ${chapter.url}")
	}

	private fun parsePagesFullimg(doc: org.jsoup.nodes.Document): List<MangaPage>? {
		for (script in doc.select("script")) {
			val data = script.html()
			val pos = data.indexOf("\"fullimg\"")
			if (pos == -1) {
				continue
			}
			val arrayBody = data.substring(pos).substringAfter('[').substringBefore(']')
			val urls = Regex("\"(https?://[^\"]+)\"").findAll(arrayBody)
				.map { it.groupValues[1] }
				.filter { it.isNotBlank() }
				.toList()
			if (urls.isEmpty()) {
				continue
			}
			return urls.map { url ->
				MangaPage(
					id = generateUid(url),
					url = url,
					preview = null,
					source = source,
				)
			}
		}
		return null
	}

	private suspend fun fetchAvailableTags(): Set<MangaTag> {
		val domain = domain
		val doc = webClient.httpGet("https://$domain/mostfavorites&sort=manga").parseHtml()
		val root = doc.body().selectFirst("div.main_fon")?.getElementById("side")
			?.select("ul")?.last() ?: doc.parseFailed("Cannot find root")
		return root.select("li.sidetag").mapToSet { li ->
			val a = li.children().lastOrNull() ?: li.parseFailed("a is null")
			MangaTag(
				title = a.text().toTagName(),
				key = a.attr("href").substringAfterLast('/').urlDecode(),
				source = source,
			)
		}
	}

	override suspend fun getUsername(): String {
		val doc = webClient.httpGet("https://${domain}").parseHtml().body()
		val root = doc.requireElementById("top_user")
		val a = root.getElementsByAttributeValueContaining("href", "/user/").firstOrNull()
			?: throw AuthRequiredException(source)
		return a.attr("href").removeSuffix('/').substringAfterLast('/')
	}

	override suspend fun getRelatedManga(seed: Manga): List<Manga> {
		val doc = webClient.httpGet(seed.url.replace("/manga/", "/related/").toAbsoluteUrl(domain)).parseHtml()
		val root = doc.body().requireElementById("right")
		return root.select("div.related").mapNotNull { div ->
			val info = div.selectFirst(".related_info") ?: return@mapNotNull null
			val a = info.selectFirst("a") ?: return@mapNotNull null
			val href = a.attrAsRelativeUrl("href")
			val title = a.text().parseTitle()
			val author = info.getElementsByAttributeValueStarting(
				"href",
				"/mangaka",
			).firstOrNull()?.text()
			Manga(
				id = generateUid(a.attrAsRelativeUrlAnyHost("href")),
				url = href,
				publicUrl = href.toAbsoluteUrl(a.host ?: domain),
				altTitles = setOfNotNull(title.second),
				title = title.first,
				authors = setOfNotNull(author),
				coverUrl = div.selectFirst("img")?.absUrl("src").orEmpty(),
				tags = emptySet(),
				rating = RATING_UNKNOWN,
				state = null,
				contentRating = if (isNsfwSource) ContentRating.ADULT else null,
				source = source,
			)
		}
	}

	protected open fun buildUrl(
		offset: Int,
		order: SortOrder,
		filter: MangaListFilter,
	): HttpUrl {
		val builder = urlBuilder()
		builder.addQueryParameter("offset", offset.toString())
		when {
			!filter.query.isNullOrEmpty() -> {
				builder.addQueryParameter("do", "search")
				builder.addQueryParameter("subaction", "search")
				builder.addQueryParameter("search_start", ((offset / 40) + 1).toString())
				builder.addQueryParameter("full_search", "0")
				builder.addQueryParameter("result_from", (offset + 1).toString())
				builder.addQueryParameter("result_num", "40")
				builder.addQueryParameter("story", filter.query)
				builder.addQueryParameter("need_sort_date", "false")
			}

			else -> {
				if (filter.tags.isNotEmpty() || filter.tagsExclude.isNotEmpty()) {
					builder.addPathSegment("tags")
					val joiner = StringUtil.StringJoiner("+")
					filter.tags.forEach { joiner.add(it.key) }
					filter.tagsExclude.forEach { joiner.add("-"); joiner.append(it.key) }
					builder.addPathSegment(joiner.complete())
					builder.addQueryParameter(
						"n",
						when (order) {
							SortOrder.RATING,
							SortOrder.POPULARITY,
								-> "favdesc"

							SortOrder.ALPHABETICAL -> "abcasc"
							else -> "" // SortOrder.NEWEST
						},
					)
				} else {
					when (order) {
						SortOrder.POPULARITY -> builder.addPathSegment("mostviews")
						SortOrder.ALPHABETICAL -> builder.addPathSegment("catalog")
						SortOrder.RATING -> builder.addPathSegment("mostfavorites")
						else -> { // SortOrder.NEWEST
							builder.addPathSegment("manga")
							builder.addPathSegment("new")
						}
					}
				}
			}
		}
		return builder.build()
	}

	private fun String.toTagName() = replace('_', ' ').toTitleCase()

	private fun String.parseTitle(): Pair<String, String?> {
		var depth = 0
		for (i in indices.reversed()) {
			val c = this[i]
			if (c == '(') {
				depth--
				if (depth == 0 && (i + 2) < lastIndex && i > 0) {
					return substring(i + 1, lastIndex).trim() to substring(0, i).trim().nullIfEmpty()
				}
			} else if (c == ')') {
				depth++
			}
		}
		return this to null
	}

	private fun Element.attrAsRelativeUrlAnyHost(attributeKey: String): String {
		val attr = attr(attributeKey)
		if (attr.isEmpty() || attr.startsWith("data:")) {
			throw IllegalArgumentException("Wrong url $attr")
		}
		if (attr.startsWith('/')) {
			return attr
		}
		val host = attr.toHttpUrl().host
		return attr.substringAfter(host)
	}

	private companion object {
		val HREF_V_CH_REGEX = Regex("""_v(\d+)_ch(\d+)""", RegexOption.IGNORE_CASE)
	}
}
