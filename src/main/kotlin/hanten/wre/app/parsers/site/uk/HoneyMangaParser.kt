package hanten.wre.app.parsers.site.uk

import androidx.collection.ArraySet
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.getFloatOrDefault
import hanten.wre.app.parsers.util.json.getIntOrDefault
import hanten.wre.app.parsers.util.json.getStringOrNull
import hanten.wre.app.parsers.util.json.mapJSON
import hanten.wre.app.parsers.util.json.mapJSONNotNull
import hanten.wre.app.parsers.util.suspendlazy.getOrNull
import hanten.wre.app.parsers.util.suspendlazy.suspendLazy
import java.text.SimpleDateFormat
import java.util.*

private const val PAGE_SIZE = 20
private const val CHAPTERS_PAGE_SIZE = 100
private const val WEBVIEW_TIMEOUT_MS = 30000L
private const val HEADER_ENCODING = "Content-Encoding"
private const val IMAGE_BASEURL_FALLBACK = "https://hmvolumestorage.b-cdn.net/public-resources"

@MangaSourceParser("HONEYMANGA", "HoneyManga", "uk")
internal class HoneyMangaParser(context: MangaLoaderContext) :
	PagedMangaParser(context, MangaParserSource.HONEYMANGA, PAGE_SIZE),
	Interceptor {

	private val urlApi get() = "https://data.api.$domain"
	private val mangaObjectApi get() = "$urlApi/manga"
	private val mangaApi get() = "$urlApi/v2/manga/cursor-list"
	private val chapterApi get() = "$urlApi/v2/chapter/cursor-list"
	private val genresListApi get() = "$urlApi/genres-tags/genres-list"
	private val framesApi get() = "$urlApi/chapter/frames"
	private val searchApi get() = "https://search.api.$domain/v2/manga/pattern?query="

	private val imageStorageUrl = suspendLazy(initializer = ::fetchCoversBaseUrl)

	override val configKeyDomain = ConfigKey.Domain("honey-manga.com.ua")

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isMultipleTagsSupported = true,
			isSearchSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = fetchAvailableTags(),
	)

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		super.onCreateConfig(keys)
		keys.add(userAgentKey)
	}

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.POPULARITY,
		SortOrder.NEWEST,
	)

	override suspend fun getDetails(manga: Manga): Manga {
		val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
		val chapters = ArrayList<MangaChapter>()
		var page = 1
		var lastResponse = JSONObject()
		// API rejects huge pageSize (999999 -> 400 "invalid pageSize"),
		// so paginate with a safe pageSize until cursorNext is null.
		while (true) {
			val body = JSONObject()
			body.put("mangaId", manga.url)
			body.put("page", page)
			body.put("pageSize", CHAPTERS_PAGE_SIZE)
			body.put("sortOrder", "ASC")
			val chapterRequest = postApi(chapterApi, body)
			lastResponse = chapterRequest
			val data = chapterRequest.optJSONArray("data") ?: break
			if (data.length() == 0) {
				break
			}
			data.mapJSONNotNull { jo ->
				val chapterId = jo.getStringOrNull("id")
					?: jo.getStringOrNull("chapterId")
					?: jo.getStringOrNull("uuid")
					?: return@mapJSONNotNull null
				val chapterNum = jo.getFloatOrDefault("chapterNum", 0f)
				val subChapterNum = jo.optInt("subChapterNum", 0)
				val number = if (subChapterNum > 0) {
					val base = if (chapterNum % 1f == 0f) chapterNum.toInt().toString() else chapterNum.toString()
					"$base.$subChapterNum".toFloatOrNull() ?: (chapterNum + subChapterNum / 10f)
				} else {
					chapterNum
				}
				val volume = jo.getIntOrDefault("volume", 0)
				val rawTitle = jo.getStringOrNull("title")?.trim()
				MangaChapter(
					id = generateUid(chapterId),
					// API returns a "title" placeholder when a chapter has no name;
					// fall back to the site's own "Том V - Розділ N" format.
					title = if (rawTitle.isNullOrEmpty() || rawTitle.equals("title", ignoreCase = true)) {
						"Том $volume - Розділ ${number.formatSimple()}"
					} else {
						rawTitle
					},
					number = number,
					volume = volume,
					url = chapterId + "|" + manga.url,
					scanlator = null,
					uploadDate = jo.getStringOrNull("lastUpdated")?.substringBefore('.')
						?.let { dateFormat.parseSafe(it) } ?: 0L,
					branch = null,
					source = source,
				)
			}.let(chapters::addAll)
			val counter = chapterRequest.optInt("counter", -1)
			if (counter > 0 && chapters.size >= counter) {
				break
			}
			// cursorNext == null means last page
			if (chapterRequest.isNull("cursorNext")) {
				break
			}
			page++
			if (page > 50) {
				// Safety guard: 50 * 100 = 5000 chapters max
				break
			}
		}
		if (chapters.isEmpty()) {
			checkEmptyChapters(manga.url, lastResponse)
		}
		return manga.copy(chapters = chapters)
	}

	private suspend fun checkEmptyChapters(mangaId: String, response: JSONObject) {
		val expected = runCatching {
			webClient.httpGet("$mangaObjectApi/$mangaId").parseJson()
				.getStringOrNull("chapters")?.toIntOrNull() ?: 0
		}.getOrDefault(0)
		if (expected > 0) {
			val keys = response.keys().asSequence().toList()
			throw ParseException(
				"Empty chapters (site has ~$expected). Response keys: $keys",
				"$chapterApi ($mangaId)",
			)
		}
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val body = JSONObject()
		body.put("page", page)
		body.put("pageSize", PAGE_SIZE)
		val sort = JSONObject()
		sort.put("sortBy", getSortKey(order))
		sort.put("sortOrder", "DESC")
		body.put("sort", sort)

		val content = when {
			filter.tags.isNotEmpty() -> {
				// Tags
				val filters = JSONArray()
				val tagFilter = JSONObject()
				tagFilter.put("filterBy", "genres")
				tagFilter.put("filterOperator", "ALL")
				val tag = JSONArray()
				filter.tags.forEach {
					tag.put(it.title)
				}
				tagFilter.put("filterValue", tag)
				filters.put(tagFilter)
				body.put("filters", filters)
				postApi(mangaApi, body).getJSONArray("data")

			}

			!filter.query.isNullOrEmpty() -> {
				// Search
				when {
					filter.query.length < 3 -> throw IllegalArgumentException(
						"The query must contain at least 3 characters (Запит має містити щонайменше 3 символи)",
					)

					page == searchPaginator.firstPage -> webClient
						.httpGet(searchApi + filter.query.urlEncoded())
						.parseJsonArray()

					else -> JSONArray()
				}
			}

			else -> {
				// Popular/Newest
				body.put("filters", JSONArray())
				postApi(mangaApi, body).getJSONArray("data")
			}
		}
		return content.mapJSON { jo ->
			val id = jo.getString("id")
			val posterUrl = jo.getString("posterUrl")
			val isNsfwSource = isNsfw(jo.getStringOrNull("adult"))
			Manga(
				id = generateUid(id),
				title = jo.getString("title"),
				altTitles = setOfNotNull(jo.getStringOrNull("alternativeTitle")),
				url = id,
				publicUrl = "https://$domain/book/$id",
				rating = RATING_UNKNOWN,
				contentRating = if (isNsfwSource) ContentRating.ADULT else null,
				coverUrl = getCoverUrl(posterUrl, 256),
				tags = getTitleTags(jo.optJSONArray("genresAndTags")),
				state = parseStatus(jo.getStringOrNull("titleStatus")),
				authors = parseAuthors(jo),
				largeCoverUrl = getCoverUrl(posterUrl, 1080),
				description = jo.getStringOrNull("description"),
				chapters = null,
				source = source,
			)
		}
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val content = fetchFrames(chapter.url).getJSONObject("resourceIds")
		val baseUrl = imageStorageUrl.getOrNull() ?: IMAGE_BASEURL_FALLBACK
		return List(content.length()) { i ->
			val item = content.getString(i.toString())
			MangaPage(id = generateUid(item), "$baseUrl/$item", getCoverUrl(item, 256), source)
		}
	}

	private suspend fun fetchFrames(chapterUrl: String): JSONObject {
		// Current format: "<chapterId>|<mangaId>".
		// NOTE: old base /chapter/frames/<cid>/<mid> returns 404,
		// use /v2/chapter/frames/<cid>/<mid> with fallback to single-id legacy call.
		val pipe = chapterUrl.split("|")
		if (pipe.size == 2) {
			val (cid, mid) = pipe
			return try {
				webClient.httpGet("$urlApi/v2/chapter/frames/$cid/$mid").parseJson()
			} catch (e: Exception) {
				webClient.httpGet("$framesApi/$cid").parseJson()
			}
		}
		// Transitional "<mangaId>/<resourcesId>" and legacy single-id formats
		val slash = chapterUrl.split("/")
		if (slash.size == 2) {
			val (mid, rid) = slash
			return try {
				webClient.httpGet("$urlApi/v2/chapter/frames/$rid/$mid").parseJson()
			} catch (e: Exception) {
				webClient.httpGet("$framesApi/$rid").parseJson()
			}
		}
		return webClient.httpGet("$framesApi/$chapterUrl").parseJson()
	}

	private suspend fun fetchAvailableTags(): Set<MangaTag> {
		// https://data.api.honey-manga.com.ua/genres-tags/genres-list
		val content = webClient.httpGet(genresListApi).parseJsonArray()
		val tagsSet = ArraySet<MangaTag>(content.length())
		repeat(content.length()) { i ->
			val item = content.getString(i)
			tagsSet.add(MangaTag(item, item, source))
		}
		return tagsSet
	}

	// Need for disable encoding (with encoding not working)
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val newRequest = if (request.header(HEADER_ENCODING) != null) {
			request.newBuilder().removeHeader(HEADER_ENCODING).build()
		} else {
			request
		}
		return chain.proceed(newRequest)
	}

	private fun isNsfw(adultValue: String?): Boolean {
		val intValue = adultValue?.removeSuffix('+')?.toIntOrNull()
		return intValue != null && intValue >= 18
	}

	private suspend fun getCoverUrl(id: String, w: Int): String {
		val baseUrl = imageStorageUrl.getOrNull() ?: IMAGE_BASEURL_FALLBACK
		return concatUrl(baseUrl, "$id?optimizer=image&width=$w&height=$w")
	}

	private fun getSortKey(order: SortOrder?) = when (order) {
		SortOrder.POPULARITY -> "likes"
		SortOrder.NEWEST -> "lastUpdated"
		else -> "likes"
	}

	private fun getTitleTags(jsonTags: JSONArray?): Set<MangaTag> {
		if (jsonTags == null) {
			return emptySet()
		}
		val tagsSet = ArraySet<MangaTag>(jsonTags.length())
		repeat(jsonTags.length()) { i ->
			val item = jsonTags.optString(i).takeIf { it.isNotEmpty() } ?: return@repeat

			tagsSet.add(MangaTag(title = item.toTitleCase(sourceLocale), key = item, source = source))
		}
		return tagsSet
	}

	private fun parseStatus(value: String?): MangaState? {
		if (value.isNullOrBlank()) {
			return null
		}
		val v = value.lowercase(Locale.ROOT)
		return when {
			"заверш" in v || "finish" in v || "complete" in v -> MangaState.FINISHED
			"анонс" in v || "upcoming" in v || "announce" in v -> MangaState.UPCOMING
			"онг" in v || "онґ" in v || "ongoing" in v -> MangaState.ONGOING
			// Legacy value "Онгоінг" also contains "онг", kept for compatibility
			else -> null
		}
	}

	private fun parseAuthors(jo: JSONObject): Set<String> {
		val result = LinkedHashSet<String>()
		jo.optJSONArray("authors")?.let { arr ->
			repeat(arr.length()) { i ->
				arr.optString(i).takeIf { it.isNotBlank() }?.let(result::add)
			}
		}
		jo.optJSONArray("artists")?.let { arr ->
			repeat(arr.length()) { i ->
				arr.optString(i).takeIf { it.isNotBlank() }?.let(result::add)
			}
		}
		return result
	}

	private suspend fun postApi(url: String, body: JSONObject): JSONObject {
		return try {
			webClient.httpPost(url, body).parseJson()
		} catch (e: kotlinx.coroutines.CancellationException) {
			throw e
		} catch (e: Exception) {
			// data.api rejects non-browser TLS fingerprints with 400;
			// retry the same call through the WebView engine (real browser stack)
			webViewPost(url, body)
		}
	}

	private suspend fun webViewPost(url: String, body: JSONObject): JSONObject {
		val payload = body.toString()
			.replace("\\", "\\\\")
			.replace("'", "\\'")
		val script = "fetch('$url',{" +
			"method:'POST'," +
			"headers:{'Content-Type':'application/json'}," +
			"body:'$payload'" +
			"}).then(r=>{if(!r.ok)throw new Error('HTTP '+r.status);return r.text()})"
		val text = context.evaluateJs("https://$domain", script, WEBVIEW_TIMEOUT_MS)
			?: throw ParseException("Empty API response", url)
		return JSONObject(text)
	}

	private suspend fun fetchCoversBaseUrl(): String {
		return try {
			val scriptUrl = webClient.httpGet("https://$domain")
				.parseHtml()
				.select("script")
				.firstNotNullOf { it.attrOrNull("src")?.takeIf { x -> x.contains("_app-") } }
			val script = webClient.httpGet(scriptUrl).parseRaw()
			// Old: "vg":"https://hmvolumestorage.b-cdn.net/public-resources"
			// New (_app chunk): En:"https://hmvolumestorage.b-cdn.net/public-resources"
			Regex("\"vg\"\\s*:\\s*\"([^\"]+)\"").find(script)?.groups?.get(1)?.value
				?: Regex("\\bEn\\s*:\\s*\"(https://[^\"]+)\"").find(script)?.groups?.get(1)?.value
				?: Regex("(https://[a-z0-9\\-./]*b-cdn\\.net/public-resources)").find(script)?.value
				?: IMAGE_BASEURL_FALLBACK
		} catch (e: Exception) {
			IMAGE_BASEURL_FALLBACK
		}
	}
}
