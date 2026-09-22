package hanten.wre.app.parsers.site.ru.rulib

import androidx.collection.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaParserAuthProvider
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.AuthRequiredException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.network.UserAgents
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.*
import hanten.wre.app.parsers.util.suspendlazy.suspendLazy
import java.text.SimpleDateFormat
import java.util.*

internal abstract class LibSocialParser(
	context: MangaLoaderContext,
	source: MangaParserSource,
	protected val siteId: Int,
	siteDomains: Array<String>,
) : PagedMangaParser(context, source, pageSize = 60), MangaParserAuthProvider {

	protected val apiHost = "api.cdnlibs.org"

	override val userAgentKey = ConfigKey.UserAgent(UserAgents.CHROME_MOBILE)

	override val authUrl: String
		get() = "https://$domain/ru/front/auth"

	override suspend fun isAuthorized(): Boolean {
		val token = getAuthData()?.optJSONObject("token")?.getStringOrNull("access_token")
		return !token.isNullOrEmpty()
	}

	override suspend fun getUsername(): String = getAuthData()
		?.getJSONObject("auth")
		?.getString("username")
		?: throw AuthRequiredException(source)

	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.UPDATED,
		SortOrder.POPULARITY,
		SortOrder.RATING,
		SortOrder.NEWEST,
		SortOrder.ALPHABETICAL,
		SortOrder.ALPHABETICAL_DESC,
	)

	final override val configKeyDomain = ConfigKey.Domain(*siteDomains)

	override val filterCapabilities: MangaListFilterCapabilities
		get() = MangaListFilterCapabilities(
			isMultipleTagsSupported = true,
			isTagsExclusionSupported = true,
			isSearchSupported = true,
			isSearchWithFiltersSupported = true,
		)

	override suspend fun getFilterOptions() = MangaListFilterOptions(
		availableTags = fetchAvailableTags(),
		availableStates = EnumSet.of(
			MangaState.ONGOING,
			MangaState.FINISHED,
			MangaState.ABANDONED,
			MangaState.PAUSED,
			MangaState.UPCOMING,
		),
	)

	final override fun intercept(chain: Interceptor.Chain): Response {
		val token = runBlocking { getAuthData() }?.optJSONObject("token")?.getStringOrNull("access_token")
		val requestBuilder = chain.request().newBuilder()
		if (!token.isNullOrEmpty()) {
			requestBuilder.header("Authorization", "Bearer $token")
		}
		requestBuilder.header("Site-Id", siteId.toString())
		return chain.proceed(requestBuilder.build())
	}

	private val statesMap = intObjectMapOf(
		1, MangaState.ONGOING,
		2, MangaState.FINISHED,
		3, MangaState.UPCOMING,
		4, MangaState.PAUSED,
		5, MangaState.ABANDONED,
	)
	private val imageServers = suspendLazy(initializer = ::fetchServers)
	private val splitTranslationsKey = ConfigKey.SplitByTranslations(true)
	private val preferredServerKey = ConfigKey.PreferredImageServer(
		presetValues = mapOf(
			null to null,
			SERVER_MAIN to "Первый",
			SERVER_SECONDARY to "Второй",
			SERVER_COMPRESS to "Сжатия",
			SERVER_DOWNLOAD to "Загрузки",
			SERVER_CROP to "Обрезки",
		),
		defaultValue = null,
	)

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		val urlBuilder = HttpUrl.Builder()
			.scheme(SCHEME_HTTPS)
			.host(apiHost)
			.addPathSegment("api")
			.addPathSegment("manga")
			.addQueryParameter("site_id[]", siteId.toString())
			.addQueryParameter("fields[]", "rate")
			.addQueryParameter("fields[]", "rate_avg")
			.addQueryParameter("page", page.toString())
		for (state in filter.states) {
			urlBuilder.addQueryParameter("status[]", statesMap.keyOf(state).toString())
		}
		for (tag in filter.tags) {
			urlBuilder.addQueryParameter("${tag.typeKey()}[]", tag.key.drop(1))
		}
		for (tag in filter.tagsExclude) {
			urlBuilder.addQueryParameter("${tag.typeKey()}_exclude[]", tag.key.drop(1))
		}
		if (!filter.query.isNullOrEmpty()) {
			urlBuilder.addQueryParameter("q", filter.query)
		}
		urlBuilder.addQueryParameter(
			"sort_by",
			when (order) {
				SortOrder.UPDATED -> "last_chapter_at"
				SortOrder.POPULARITY -> "views"
				SortOrder.RATING -> "rate_avg"
				SortOrder.NEWEST -> "created_at"
				SortOrder.ALPHABETICAL,
				SortOrder.ALPHABETICAL_DESC,
					-> "rus_name"

				else -> null
			},
		)
		urlBuilder.addQueryParameter(
			"sort_type",
			when (order) {
				SortOrder.UPDATED,
				SortOrder.POPULARITY,
				SortOrder.RATING,
				SortOrder.NEWEST,
				SortOrder.ALPHABETICAL_DESC,
					-> "desc"

				SortOrder.ALPHABETICAL -> "asc"
				else -> null
			},
		)
		val json = webClient.httpGet(urlBuilder.build()).parseJson()
		val data = json.getJSONArray("data")
		return data.mapJSON(::parseManga)
	}

	override suspend fun getDetails(manga: Manga): Manga = coroutineScope {
		val chaptersDeferred = async { fetchChapters(manga) }
		val url = HttpUrl.Builder()
			.scheme(SCHEME_HTTPS)
			.host(apiHost)
			.addPathSegment("api")
			.addPathSegment("manga")
			.addPathSegment(manga.url)
			.addQueryParameter("fields[]", "summary")
			.addQueryParameter("fields[]", "genres")
			.addQueryParameter("fields[]", "tags")
			.addQueryParameter("fields[]", "authors")
			.addQueryParameter("fields[]", "close_view")
			.build()
		val json = webClient.httpGet(url).parseJson().getJSONObject("data")
		val genres = json.getJSONArray("genres").mapJSON { jo ->
			MangaTag(title = jo.getString("name"), key = "g" + jo.getInt("id"), source = source)
		}
		val tags = json.getJSONArray("tags").mapJSON { jo ->
			MangaTag(title = jo.getString("name"), key = "t" + jo.getInt("id"), source = source)
		}
		val authors = json.getJSONArray("authors").mapJSONNotNullToSet {
			it.getStringOrNull("name")
		}
		val chapters = chaptersDeferred.await()
		val isRestricted = json.getIntOrDefault("close_view", 0) > 0
		manga.copy(
			title = json.getStringOrNull("rus_name") ?: manga.title,
			altTitles = setOfNotNull(json.getStringOrNull("name")),
			tags = tagsSetOf(tags, genres),
			state = if (chapters.isEmpty() && isRestricted) {
				MangaState.RESTRICTED
			} else {
				manga.state
			},
			authors = authors,
			contentRating = json.optJSONObject("ageRestriction")?.let {
				val id = it.getIntOrDefault("id", -1)
				if (id >= 4) ContentRating.SUGGESTIVE else sourceContentRating
			} ?: manga.contentRating,
			description = json.parseSummary()?.nl2br(),
			chapters = chapters,
		)
	}

	private fun JSONObject.parseSummary(): String? {
		// getStringOrNull() stringifies ANY value, so a TipTap object would leak
		// as raw JSON text — check the actual type first.
		return when (val raw = opt("summary")) {
			null, JSONObject.NULL -> null
			is String -> raw.takeIf { it.isNotBlank() }
			is JSONObject -> parseTipTapDoc(raw)
			else -> null
		}
	}

	private fun parseTipTapDoc(doc: JSONObject): String? {
		// TipTap doc: {"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"..."}]}]}
		return buildString {
			doc.optJSONArray("content")?.asTypedList<JSONObject>()?.forEach { block ->
				block.optJSONArray("content")?.asTypedList<JSONObject>()?.forEach { node ->
					node.getStringOrNull("text")?.let(::append)
				}
				append('\n')
			}
		}.trim().takeIf { it.isNotEmpty() }
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> = coroutineScope {
		val pages = async {
			webClient.httpGet(
				concatUrl("https://$apiHost/api/manga/", chapter.url),
			).parseJson().getJSONObject("data")
		}
		val servers = imageServers.get()
		val json = pages.await()
		val primaryServer = getPrimaryImageServer(servers)
		json.getJSONArray("pages").mapJSON { jo ->
			val url = jo.getString("url")
			MangaPage(
				id = generateUid(jo.getLong("id")),
				url = concatUrl(primaryServer, url),
				preview = servers[SERVER_COMPRESS]?.let { concatUrl(it, url) },
				source = source,
			)
		}
	}

	private suspend fun fetchAvailableTags(): Set<MangaTag> = coroutineScope {
		val tags = async { fetchTags("tags") }
		val genres = async { fetchTags("genres") }
		tagsSetOf(tags.await(), genres.await())
	}

	override suspend fun getRelatedManga(seed: Manga): List<Manga> {
		val json = webClient.httpGet(
			HttpUrl.Builder()
				.scheme(SCHEME_HTTPS)
				.host(apiHost)
				.addPathSegment("api")
				.addPathSegment("manga")
				.addPathSegment(seed.url)
				.addPathSegment("similar")
				.build(),
		).parseJson().getJSONArray("data")
		return json.mapJSON { jo ->
			parseManga(jo.getJSONObject("media"))
		}
	}

	override suspend fun resolveLink(resolver: LinkResolver, link: HttpUrl): Manga? {
		return resolver.resolveManga(this, link.pathSegments.lastOrNull() ?: return null)
	}

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		super.onCreateConfig(keys)
		keys.add(splitTranslationsKey)
		keys.add(preferredServerKey)
	}

	private fun parseManga(jo: JSONObject): Manga {
		val cover = jo.getJSONObject("cover")
		val isNsfwSource = jo.getJSONObject("ageRestriction").getIntOrDefault("id", 0) >= 3
		return Manga(
			id = generateUid(jo.getLong("id")),
			title = jo.getString("rus_name").ifEmpty { jo.getString("name") },
			altTitles = setOfNotNull(jo.getString("name")),
			url = jo.getString("slug_url"),
			publicUrl = "https://$domain/ru/manga/" + jo.getString("slug_url"),
			rating = jo.optJSONObject("rating")
				?.getFloatOrDefault("average", RATING_UNKNOWN * 10f)?.div(10f) ?: RATING_UNKNOWN,
			contentRating = sourceContentRating,
			coverUrl = cover.getString("thumbnail"),
			tags = setOf(),
			state = statesMap[jo.optJSONObject("status")?.getIntOrDefault("id", -1) ?: -1],
			authors = emptySet(),
			largeCoverUrl = cover.getString("default"),
			source = source,
		)
	}

	private fun getPrimaryImageServer(servers: ScatterMap<String, String>): String {
		val preferred = config[preferredServerKey]
		if (preferred != null) {
			servers[preferred]?.let { return it }
		}
		// NOTE: img*.imglib.info (main/secondary) currently answers 403 to everyone;
		// prefer alive img*.cdnlibs.org hosts (download/compress) by default.
		return checkNotNull(
			servers[SERVER_DOWNLOAD] ?: servers[SERVER_COMPRESS]
				?: servers[SERVER_MAIN] ?: servers[SERVER_SECONDARY],
		) {
			"No available images servers"
		}
	}

	private suspend fun fetchChapters(manga: Manga): List<MangaChapter> {
		val url = HttpUrl.Builder()
			.scheme(SCHEME_HTTPS)
			.host(apiHost)
			.addPathSegment("api")
			.addPathSegment("manga")
			.addPathSegment(manga.url)
			.addPathSegment("chapters")
			.build()
		val json = webClient.httpGet(url).parseJson().getJSONArray("data")
		val builder = ChaptersListBuilder(json.length())
		val useBranching = config[splitTranslationsKey]
		for (i in 0 until json.length()) {
			val jo = json.getJSONObject(i)
			val volume = jo.getIntOrDefault("volume", 0)
			val number = jo.getFloatOrDefault("number", 0f)
			val numberString = number.formatSimple()
			val name = jo.getStringOrNull("name")
			val branches = jo.getJSONArray("branches")
			for (j in 0 until branches.length()) {
				val bjo = branches.getJSONObject(j)
				val isRestricted = bjo.optJSONObject("restricted_view")?.let {
					!it.getBooleanOrDefault("is_open", true)
				} ?: false
				if (isRestricted) {
					continue
				}
				val id = bjo.getLong("id")
				val branchId = bjo.getLongOrDefault("branch_id", 0L)
				val team = bjo.getJSONArray("teams").optJSONObject(0)?.getStringOrNull("name")
				builder += MangaChapter(
					id = generateUid(id),
					title = name,
					number = number,
					volume = volume,
					url = buildString {
						append(manga.url)
						append("/chapter?number=")
						append(numberString)
						append("&volume=")
						append(volume)
						if (branchId != 0L) {
							append("&branch_id=")
							append(branchId)
						}
					},
					scanlator = team,
					uploadDate = parseLibDate(bjo.getStringOrNull("created_at")),
					branch = if (useBranching) team else null,
					source = source,
				)
			}
		}
		return builder.toList()
	}

	private suspend fun fetchTags(type: String): List<MangaTag> {
		val data = webClient.httpGet(
			HttpUrl.Builder()
				.scheme(SCHEME_HTTPS)
				.host(apiHost)
				.addPathSegment("api").addPathSegment(type).build(),
		).parseJson().getJSONArray("data")
		val prefix = type.first().toString()
		return data.mapJSONNotNull { jo ->
			val sites = jo.getJSONArray("site_ids").toIntSet()
			if (siteId !in sites) {
				return@mapJSONNotNull null
			}
			MangaTag(
				title = jo.getString("name"),
				key = prefix + jo.getInt("id"),
				source = source,
			)
		}
	}

	private suspend fun fetchServers(): ScatterMap<String, String> {
		val json = webClient.httpGet(
			HttpUrl.Builder()
				.scheme(SCHEME_HTTPS)
				.host(apiHost)
				.addPathSegment("api")
				.addPathSegment("constants")
				.addQueryParameter("fields[]", "imageServers")
				.build(),
		).parseJson().getJSONObject("data").getJSONArray("imageServers")
		val result = MutableScatterMap<String, String>()
		for (i in 0 until json.length()) {
			val jo = json.getJSONObject(i)
			val sites = jo.getJSONArray("site_ids").toIntSet()
			if (siteId !in sites) {
				continue
			}
			result[jo.getString("id")] = jo.getString("url")
		}
		return result
	}

	private fun parseLibDate(raw: String?): Long {
		if (raw.isNullOrBlank()) {
			return 0L
		}
		// API sends microseconds (2016-12-16T09:16:50.000000Z) but SimpleDateFormat
		// understands only millis — trim the fraction to 3 digits first.
		val normalized = MICROSECONDS_REGEX.replace(raw.trim()) { match ->
			match.groupValues[1] + match.groupValues[2]
		}
		return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parseSafe(normalized)
	}

	private fun <V> IntObjectMap<V>.keyOf(value: V): Int {		forEach { k, v ->
			if (v == value) {
				return k
			}
		}
		throw NoSuchElementException("No key associated with value $value")
	}

	private fun JSONArray.toIntSet(): IntSet {
		val result = MutableIntSet(length())
		for (i in 0 until length()) {
			result.add(getInt(i))
		}
		return result
	}

	private fun MangaTag.typeKey() = when (key.firstOrNull()) {
		'g' -> "genres"
		't' -> "tags"
		else -> throw IllegalArgumentException("Tag $key($title) is of unknown type")
	}

	private fun tagsSetOf(tags: Collection<MangaTag>, genres: Collection<MangaTag>): Set<MangaTag> {
		val result = ArraySet<MangaTag>(tags.size + genres.size)
		val names = HashSet<String>(tags.size + genres.size)
		genres.forEach { x -> if (names.add(x.title)) result.add(x) }
		tags.forEach { x -> if (names.add(x.title)) result.add(x) }
		return result
	}

	private suspend fun getAuthData(): JSONObject? {
		// localStorage read spins up a WebView + page load (seconds per call) and runs
		// inside intercept() on EVERY request: cache it briefly instead.
		if (System.currentTimeMillis() - cachedAuthDataAt < AUTH_CACHE_TTL_MILLIS) {
			return cachedAuthData
		}
		val fresh = runCatching {
			val raw = WebViewHelper(context).getLocalStorageValue(domain, "auth") ?: return@runCatching null
			JSONObject(raw.unescapeJson().removeSurrounding('"'))
		}.getOrNull()
		cachedAuthData = fresh
		cachedAuthDataAt = System.currentTimeMillis()
		return fresh
	}

	@Volatile
	private var cachedAuthData: JSONObject? = null

	@Volatile
	private var cachedAuthDataAt: Long = 0L

	protected companion object {

		private const val AUTH_CACHE_TTL_MILLIS = 60_000L

		private val MICROSECONDS_REGEX = Regex("(\\.\\d{3})\\d+(Z|[+-]\\d{2}:?\\d{2})?$")

		const val SERVER_MAIN = "main"
		const val SERVER_SECONDARY = "secondary"
		const val SERVER_COMPRESS = "compress"
		const val SERVER_DOWNLOAD = "download"
		const val SERVER_CROP = "crop"
	}
}
