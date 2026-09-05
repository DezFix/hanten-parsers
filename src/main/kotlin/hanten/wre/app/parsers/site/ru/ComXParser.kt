package hanten.wre.app.parsers.site.ru

import org.json.JSONObject
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.AuthRequiredException
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.getFloatOrDefault
import hanten.wre.app.parsers.util.json.getIntOrDefault
import hanten.wre.app.parsers.util.json.getStringOrNull
import hanten.wre.app.parsers.util.suspendlazy.getOrNull
import hanten.wre.app.parsers.util.suspendlazy.suspendLazy
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("COMX", "Com-X", "ru", ContentType.COMICS)
internal class ComXParser(context: MangaLoaderContext) :
	PagedMangaParser(context, MangaParserSource.COMX, 20) {

	override val configKeyDomain = ConfigKey.Domain("com-x.life", "comx.life")

	private val availableTags = suspendLazy(initializer = ::fetchTags)
	private val cdnImageUrl = "img.com-x.life/comix/"

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		super.onCreateConfig(keys)
		keys.add(userAgentKey)
	}

	init {
		context.cookieJar.insertCookies(domain, "adt-accepted", "1")
	}

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(SortOrder.UPDATED)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = availableTags.get(),
	)

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val path = StringBuilder()
		when {
			!filter.query.isNullOrEmpty() -> {
				path.append("/search/")
				path.append(filter.query.urlEncoded())
				if (page > 1) {
					path.append("/page/$page/")
				}
			}

			filter.tags.size == 1 -> {
				path.append("/genre/")
				path.append(filter.tags.single().key.urlEncoded())
				path.append('/')
				if (page > 1) {
					path.append("page/$page/")
				}
			}

			else -> {
				path.append("/comix-read/")
				if (page > 1) {
					path.append("page/$page/")
				}
			}
		}
		val doc = webClient.httpGet(path.toString().toAbsoluteUrl(domain)).parseHtml()
		checkAuth(doc)
		val root = doc.body().selectFirst("#dle-content") ?: doc.body()
		// New poster grid; legacy .readed blocks kept as fallback
		val posters = root.select("a.poster[href]")
		if (posters.isNotEmpty()) {
			return posters.mapNotNull(::parsePoster)
		}
		val legacy = root.select(".readed .readed__title a[href], li.latest.grid-item a[href*=.html]")
		if (legacy.isNotEmpty()) {
			return legacy.mapNotNull(::parseLegacyLink)
		}
		doc.parseFailed("No manga items found")
	}

	private fun parsePoster(a: Element): Manga? {
		val href = a.attrAsRelativeUrl("href")
		if (!href.endsWith(".html")) {
			return null
		}
		val titleElement = a.selectFirst(".poster__title")
			?: a.closest(".poster")?.selectFirst(".poster__title")
			?: return null
		val (mainTitle, altTitle) = splitTitle(titleElement.text())
		val img = a.selectFirst("img")
		val cover = img?.attrAsAbsoluteUrlOrNull("data-src")
			?: img?.attrAsAbsoluteUrlOrNull("src")
		return Manga(
			id = generateUid(href),
			url = href,
			publicUrl = a.attrAsAbsoluteUrl("href"),
			title = mainTitle.ifEmpty { return null },
			altTitles = if (altTitle.isNotEmpty()) setOf(altTitle) else emptySet(),
			authors = emptySet(),
			description = null,
			tags = emptySet(),
			rating = a.selectFirst(".poster__label--rate")?.ownText()
				?.toFloatOrNull()?.div(5f) ?: RATING_UNKNOWN,
			state = null,
			coverUrl = cover,
			contentRating = if (isNsfwSource) ContentRating.ADULT else null,
			source = source,
		)
	}

	private fun parseLegacyLink(a: Element): Manga? {
		val href = a.attrAsRelativeUrl("href")
		if (!href.endsWith(".html")) {
			return null
		}
		val (mainTitle, altTitle) = splitTitle(a.text())
		val container = a.closest(".readed") ?: a.closest(".latest")
		val img = container?.selectFirst("img")
		return Manga(
			id = generateUid(href),
			url = href,
			publicUrl = a.attrAsAbsoluteUrl("href"),
			title = mainTitle.ifEmpty { return null },
			altTitles = if (altTitle.isNotEmpty()) setOf(altTitle) else emptySet(),
			authors = emptySet(),
			description = null,
			tags = emptySet(),
			rating = RATING_UNKNOWN,
			state = null,
			coverUrl = img?.attrAsAbsoluteUrlOrNull("data-src")
				?: img?.attrAsAbsoluteUrlOrNull("src"),
			contentRating = if (isNsfwSource) ContentRating.ADULT else null,
			source = source,
		)
	}

	private fun splitTitle(raw: String): Pair<String, String> {
		val parts = raw.split("\\s*/\\s*".toRegex()).map { it.trim() }
		return when {
			parts.size >= 2 -> parts[1] to parts[0]
			parts.isNotEmpty() -> parts[0] to ""
			else -> "" to ""
		}
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain)).parseHtml()
		checkAuth(doc)

		val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)

		val scriptData = doc.select("script").firstNotNullOfOrNull { script ->
			val data = script.data()
			if ("\"chapters\":" in data && "news_id" in data) data else null
		}?.substringAfter("window.__DATA__ = ")?.substringBefore(";</script>")?.trim()
			?: throw ParseException("Script data not found", manga.url)

		val jsonData = JSONObject(scriptData)
		val chaptersJson = jsonData.getJSONArray("chapters")
		val newsId = jsonData.getLong("news_id")

		val chapters = List(chaptersJson.length()) { i ->
			val chapter = chaptersJson.getJSONObject(i)
			val chapterId = chapter.getLong("id")

			MangaChapter(
				id = generateUid("$newsId/$chapterId"),
				url = "/reader/$newsId/$chapterId",
				number = chapter.getFloatOrDefault("number", chapter.getFloatOrDefault("posi", 0f)),
				title = decodeText(chapter.getStringOrNull("title")),
				uploadDate = dateFormat.parseSafe(chapter.getStringOrNull("date")),
				source = source,
				scanlator = null,
				branch = null,
				volume = chapter.getIntOrDefault("volume", 0),
			)
		}.reversed()

		val info = doc.select("ul.page__list li").associate { li ->
			val label = li.selectFirst("div")?.text()?.removeSuffix(":")?.trim().orEmpty()
			label to li.text().substringAfter(label).removePrefix(":").trim()
		}
		val authors = buildSet {
			info["Автор"]?.takeUnless { it.isEmpty() }?.let(::add)
			info["Художник"]?.takeUnless { it.isEmpty() }?.let(::add)
		}
		val state = when {
			info["Статус"]?.contains("Продолжается", ignoreCase = true) == true -> MangaState.ONGOING
			info["Статус"]?.contains("Заверш", ignoreCase = true) == true -> MangaState.FINISHED
			info["Статус"]?.contains("Анонс", ignoreCase = true) == true -> MangaState.UPCOMING
			info["Статус"]?.contains("Заморожен", ignoreCase = true) == true -> MangaState.PAUSED
			else -> null
		}

		val tags = doc.select("a[href*=/genre/]").mapNotNullToSet { a ->
			val slug = a.attr("href").removeSuffix("/").substringAfterLast("/")
				.urlDecode().takeIf { it.isNotEmpty() } ?: return@mapNotNullToSet null
			MangaTag(
				title = a.text().trim().toTitleCase(sourceLocale).takeIf { it.isNotEmpty() } ?: return@mapNotNullToSet null,
				key = slug,
				source = source,
			)
		}

		return manga.copy(
			title = doc.selectFirst("h1")?.text()?.trim()?.takeUnless { it.isEmpty() } ?: manga.title,
			authors = authors,
			state = state,
			chapters = chapters,
			description = doc.selectFirst("div.page__text.full-text.clearfix")?.textOrNull(),
			coverUrl = doc.selectFirst(".page__poster img")?.attrAsAbsoluteUrlOrNull("src") ?: manga.coverUrl,
			tags = tags.ifEmpty { manga.tags },
		)
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val newsId = chapter.url.substringAfter("/reader/").substringBefore("/")
		context.cookieJar.insertCookies(domain, "adult=$newsId")

		val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain)).parseHtml()
		checkAuth(doc)
		val scriptData = doc.select("script").firstNotNullOfOrNull { script ->
			val data = script.data()
			if ("\"images\":" in data) data else null
		} ?: throw ParseException("Image data not found", chapter.url)
		val host = """"host"\s*:\s*"([^"]+)"""".toRegex()
			.find(scriptData)?.groupValues?.get(1)?.takeUnless { it.isEmpty() }
			?: cdnImageUrl.removeSuffix("/")
		val data = scriptData
			.substringAfter("\"images\":[")
			.substringBefore("]")
			.split(",")
			.map { it.trim().removeSurrounding("\"").replace("\\", "") }
			.filter { it.isNotEmpty() }
			.ifEmpty { throw ParseException("Image data not found", chapter.url) }

		return data.map { imageUrl ->
			val finalUrl = "https://$host/comix/$imageUrl"
			MangaPage(
				id = generateUid(imageUrl),
				url = finalUrl,
				preview = null,
				source = source,
			)
		}
	}

	private suspend fun fetchTags(): Set<MangaTag> {
		val doc = runCatchingCancellable {
			webClient.httpGet("https://$domain/comix-read/").parseHtml()
		}.getOrNull() ?: return emptySet()
		return doc.select("a[href*=/genre/]").mapNotNullToSet { a ->
			val slug = a.attr("href").removeSuffix("/").substringAfterLast("/")
				.urlDecode().takeIf { it.isNotEmpty() } ?: return@mapNotNullToSet null
			val title = a.text().trim().takeIf { it.isNotEmpty() } ?: return@mapNotNullToSet null
			MangaTag(
				key = slug,
				title = title.toTitleCase(sourceLocale),
				source = source,
			)
		}
	}

	private fun checkAuth(doc: Document) {
		if (doc.body().selectFirst("#dle-content") == null &&
			(doc.title().contains("вход", ignoreCase = true) ||
				doc.body().text().contains("Тогда заходи"))
		) {
			throw AuthRequiredException(source)
		}
	}

	private fun decodeText(text: String?): String? {
		if (text == null) return null
		return try {
			text.replace("\\u([0-9a-fA-F]{4})".toRegex()) { matchResult ->
				val codePoint = matchResult.groupValues[1].toInt(16)
				codePoint.toChar().toString()
			}
		} catch (e: Exception) {
			text
		}
	}
}
