package hanten.wre.app.parsers.site.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import org.json.JSONObject
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
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
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.json.mapJSON
import hanten.wre.app.parsers.util.json.mapJSONNotNullToSet
import hanten.wre.app.parsers.util.json.mapJSONToSet
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.parseJson
import hanten.wre.app.parsers.util.parseSafe
import hanten.wre.app.parsers.util.nullIfEmpty
import hanten.wre.app.parsers.util.oneOrThrowIfMany
import hanten.wre.app.parsers.util.toTitleCase
import hanten.wre.app.parsers.util.urlEncoded
import java.text.SimpleDateFormat
import java.util.EnumSet
import java.util.Locale

@MangaSourceParser("MANGAKIO", "MangaKIO", "en", ContentType.MANGA)
internal class MangaK(context: MangaLoaderContext) :
	PagedMangaParser(context, MangaParserSource.MANGAKIO, pageSize = 24) {

	override val configKeyDomain = ConfigKey.Domain("mangak.io")

	private val apiUrl = "https://api.mangak.io"

	private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		super.onCreateConfig(keys)
		keys.add(userAgentKey)
	}

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.UPDATED,
		SortOrder.NEWEST,
		SortOrder.POPULARITY,
		SortOrder.RATING,
	)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isSearchSupported = true,
			isSearchWithFiltersSupported = true,
			isMultipleTagsSupported = true,
			isTagsExclusionSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = fetchTags(),
		availableStates = EnumSet.of(MangaState.ONGOING, MangaState.FINISHED),
	)

	private suspend fun fetchTags(): Set<MangaTag> {
		val json = webClient.httpGet("$apiUrl/genres").parseJson()
		return json.getJSONObject("data").getJSONArray("items").mapJSONToSet { item ->
			MangaTag(
				title = item.getString("name").toTitleCase(sourceLocale),
				key = item.getString("slug"),
				source = source,
			)
		}
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val url = buildString {
			append(apiUrl)
			append("/titles/search?page=")
			append(page)
			append("&limit=")
			append(pageSize)
			append("&sort=")
			append(
				when (order) {
					SortOrder.UPDATED -> "latest"
					SortOrder.NEWEST -> "newest"
					SortOrder.POPULARITY -> "popular"
					SortOrder.RATING -> "rating"
					else -> "latest"
				},
			)
			if (!filter.query.isNullOrEmpty()) {
				append("&q=")
				append(filter.query.urlEncoded())
			}
			if (filter.tags.isNotEmpty()) {
				append("&genres=")
				append(filter.tags.joinToString(",") { it.key })
			}
			if (filter.tagsExclude.isNotEmpty()) {
				append("&exclude=")
				append(filter.tagsExclude.joinToString(",") { it.key })
			}
			filter.states.oneOrThrowIfMany()?.let {
				append("&status=")
				append(
					when (it) {
						MangaState.ONGOING -> "ongoing"
						MangaState.FINISHED -> "completed"
						else -> "all"
					},
				)
			}
		}
		val json = webClient.httpGet(url).parseJson()
		return json.getJSONObject("data").getJSONArray("items").mapJSON { item ->
			item.toManga()
		}
	}

	private fun JSONObject.toManga(): Manga {
		val relativeUrl = getString("url")
		return Manga(
			id = generateUid(getString("id")),
			title = getString("name"),
			altTitles = emptySet(),
			url = getString("id"),
			publicUrl = "https://$domain$relativeUrl",
			rating = optDouble("rating", 0.0).toRating(),
			contentRating = null,
			coverUrl = optString("cover").nullIfEmpty(),
			tags = optJSONArray("genres")?.mapJSONNotNullToSet { genre ->
				val slug = genre.optString("slug").nullIfEmpty() ?: return@mapJSONNotNullToSet null
				MangaTag(genre.getString("name").toTitleCase(sourceLocale), slug, source)
			}.orEmpty(),
			state = optString("status").toMangaState(),
			authors = emptySet(),
			description = optString("summary").nullIfEmpty(),
			source = source,
		)
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val json = webClient.httpGet("$apiUrl/titles/${manga.url}").parseJson()
			.getJSONObject("data").getJSONObject("title")
		val cv = json.optLong("cv")
		val authors = json.optJSONArray("authors")?.mapJSONNotNullToSet {
			it.optString("name").nullIfEmpty()
		}.orEmpty()
		val artists = json.optJSONArray("artists")?.mapJSONNotNullToSet {
			it.optString("name").nullIfEmpty()
		}.orEmpty()
		val tags = json.optJSONArray("genres")?.mapJSONNotNullToSet { genre ->
			val slug = genre.optString("slug").nullIfEmpty() ?: return@mapJSONNotNullToSet null
			MangaTag(genre.getString("name").toTitleCase(sourceLocale), slug, source)
		}.orEmpty()
		val altTitles = json.optString("alt_name").split(";")
			.mapNotNull { it.trim().nullIfEmpty() }
			.toSet()
		return manga.copy(
			title = json.optString("name").nullIfEmpty() ?: manga.title,
			altTitles = altTitles,
			description = json.optString("summary").nullIfEmpty() ?: manga.description,
			tags = tags.ifEmpty { manga.tags },
			authors = authors + artists,
			state = json.optString("status").toMangaState() ?: manga.state,
			rating = json.optDouble("rating", 0.0).toRating(),
			chapters = fetchChapters(manga.url, cv),
		)
	}

	private suspend fun fetchChapters(id: String, cv: Long): List<MangaChapter> {
		val json = webClient.httpGet("$apiUrl/titles/$id/chapters?cv=$cv").parseJson()
		val chapters = json.getJSONObject("data").getJSONArray("chapters").mapJSON { it }
		val total = chapters.size
		return chapters.mapIndexed { index, chapter ->
			MangaChapter(
				id = generateUid(chapter.getString("id")),
				title = chapter.optString("name").nullIfEmpty(),
				number = chapter.optDouble("chapter_number", Double.NaN).takeUnless { it.isNaN() }?.toFloat()
					?: (total - index).toFloat(),
				volume = 0,
				url = chapter.getString("url"),
				scanlator = null,
				uploadDate = dateFormat.parseSafe(chapter.optString("updated_at").substringBefore('.')),
				branch = null,
				source = source,
			)
		}.reversed()
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val doc = webClient.httpGet("https://$domain${chapter.url}").parseHtml()
		val raw = doc.selectFirst("script#__NEXT_DATA__")?.data()
			?: throw ParseException("Cannot find chapter data", chapter.url)
		val images = JSONObject(raw)
			.getJSONObject("props")
			.getJSONObject("pageProps")
			.getJSONObject("initialChapter")
			.getJSONArray("images")
		return (0 until images.length()).map { i ->
			val imageUrl = images.getString(i)
			MangaPage(
				id = generateUid(imageUrl),
				url = imageUrl,
				preview = null,
				source = source,
			)
		}
	}

	private fun Double.toRating(): Float = if (this > 0.0) (this / 5.0).toFloat() else RATING_UNKNOWN

	private fun String?.toMangaState(): MangaState? = when (this?.lowercase(Locale.US)) {
		"ongoing" -> MangaState.ONGOING
		"completed" -> MangaState.FINISHED
		else -> null
	}
}
