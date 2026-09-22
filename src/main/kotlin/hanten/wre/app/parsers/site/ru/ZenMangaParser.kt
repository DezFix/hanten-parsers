package hanten.wre.app.parsers.site.ru

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.json.JSONArray
import org.json.JSONObject
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.MangaParserAuthProvider
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.network.CloudFlareHelper
import hanten.wre.app.parsers.network.UserAgents
import hanten.wre.app.parsers.util.*
import hanten.wre.app.parsers.util.json.getStringOrNull
import hanten.wre.app.parsers.util.json.mapJSON
import hanten.wre.app.parsers.util.json.unescapeJson
import hanten.wre.app.parsers.Broken
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("ZENMANGA", "ZenManga", "ru")
internal class ZenMangaParser(context: MangaLoaderContext) :
	PagedMangaParser(context, MangaParserSource.ZENMANGA, 30),
	MangaParserAuthProvider {

	private val astroJsonParser = AstroJsonParser()
	private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

	private val dateFormatShort = SimpleDateFormat("dd.MM.yyyy", Locale.US)

	private companion object {

		val CONTENT_HREF_REGEX = Regex("^/content/[a-z0-9-]+$")

		val descriptionRegex = Regex("description:\"((?:[^\"\\\\]|\\\\.)*)\"")

		val chapterObjectRegex = Regex("\\{id:\"([0-9a-f-]{36})\"")
		val nameRegex = Regex("name:\"((?:[^\"\\\\]|\\\\.)*)\"")
		val titleRegex = Regex("title:\"((?:[^\"\\\\]|\\\\.)*)\"")
		val numberRegex = Regex("number:([0-9.]+)")
		val volumeRegex = Regex("volume:(\\d+)")
		val branchRegex = Regex("branchId:\"([0-9a-f-]*)\"")
		val publisherRegex = Regex("publisherNames:\"((?:[^\"\\\\]|\\\\.)*)\"")
		val createdRegex = Regex("createdAt:\"([^\"]+)\"")
		val dateRegex = Regex("date:\"([\\d.]+)\"")
		val authorRegex =
			Regex("type:\"(?:AUTHOR|ARTIST)\",publisher:[^}]*?name:\"((?:[^\"\\\\]|\\\\.)*)\"")
	}

	init {
		setFirstPage(0)
	}

	override val configKeyDomain = ConfigKey.Domain("inkstory.net", "inkstory.me")

	// Only popularity order is backed by the SSR catalog for now
	override val availableSortOrders: Set<SortOrder> = EnumSet.of(
		SortOrder.POPULARITY,
	)

	override val filterCapabilities: MangaListFilterCapabilities = MangaListFilterCapabilities(
		isSearchSupported = true,
	)

	override val authUrl: String
		get() = "https://sso.inuko.me/account/sign-in"

	private val apiDomain = "api.inuko.me"

	private fun checkAuth(): Boolean {
		val authCookieName = "__otaku_session"
		return context.cookieJar.getCookies("inkstory.net").any { it.name == authCookieName } ||
			context.cookieJar.getCookies("inuko.me").any { it.name == authCookieName }
	}

	override suspend fun isAuthorized(): Boolean {
		return checkAuth()
	}

	override suspend fun getUsername(): String {
		val libraryUrl = "/library"
		val data = fetchAstroData(libraryUrl)
			?: throw ParseException("Не удалось получить Astro JSON для получения имени пользователя", libraryUrl)

		val session = data["session"] as? Map<*, *>
			?: throw ParseException("Ключ 'session' не найден", libraryUrl)

		val currentUser = session["currentUser"] as? Map<*, *>
			?: throw ParseException("Ключ 'currentUser' не найден", libraryUrl)

		return currentUser["username"] as? String
			?: throw ParseException("Ключ 'username' не найден", libraryUrl)
	}

	override suspend fun getListPage(page: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		// The v2/books API is gone (api.inkstory.net dead, api.inuko.me/books returns []),
		// so the list comes from the server-rendered catalog. Only pagination
		// and text search are supported this way; details fill the rest.
		// Note: ?page=N renders content page N+1, matching our 0-based index.
		val urlBuilder = "https://$domain/content".toHttpUrl().newBuilder()
			.addQueryParameter("page", page.toString())
		filter.query?.takeIf { it.isNotBlank() }?.let {
			urlBuilder.addQueryParameter("search", it)
		}
		val doc = webClient.httpGet(urlBuilder.build()).parseHtml()
		val result = ArrayList<Manga>()
		val seen = HashSet<String>()
		for (a in doc.select("a[href]")) {
			val href = a.attr("href")
			if (!CONTENT_HREF_REGEX.matches(href) || !seen.add(href)) {
				continue
			}
			// Cards carry a poster image; nav/service links (like /content/top) don't
			val img = a.selectFirst("img") ?: continue
			val title = img.attr("alt").trim().takeUnless { it.isEmpty() }
				?: a.text().trim().takeUnless { it.isEmpty() }
				?: continue
			val cover = img.attr("src").trim().takeUnless { it.isEmpty() }
			val publicUrl = href.toAbsoluteUrl(domain)
			result.add(
				Manga(
					id = generateUid(publicUrl),
					url = href,
					publicUrl = publicUrl,
					title = title,
					altTitles = emptySet(),
					coverUrl = cover,
					source = source,
					rating = RATING_UNKNOWN,
					state = null,
					contentRating = null,
					tags = emptySet(),
					authors = emptySet(),
				),
			)
		}
		return result
	}

	override suspend fun getDetails(manga: Manga): Manga {
		val slug = manga.url.substringAfterLast('/')
		val doc = webClient.httpGet("https://$domain/content/$slug", getRequestHeaders()).parseHtml()
		val rawHtml = doc.html()
		val description = descriptionRegex.find(rawHtml)?.groupValues?.get(1)
			?.unescapeJson()?.takeUnless { it.isBlank() }
			?: doc.selectFirst("meta[property=og:description]")?.attr("content")?.trim()
		val tags = doc.select("a[href^=/genres/]").mapNotNullTo(HashSet()) { a ->
			val title = a.text().trim().replaceFirstChar { c -> c.uppercase() }.takeIf { it.isNotEmpty() }
				?: return@mapNotNullTo null
			val key = a.attr("href").removeSuffix("/").substringAfterLast("/").takeIf { it.isNotEmpty() }
				?: return@mapNotNullTo null
			MangaTag(key = key, title = title, source = source)
		}
		val chaptersDoc = webClient.httpGet(
			"https://$domain/content/$slug/chapters",
			getRequestHeaders(),
		).parseHtml().html()
		val authors = authorRegex.findAll(chaptersDoc).mapNotNullTo(HashSet()) { match ->
			match.groupValues[1].unescapeJson().takeUnless { it.isBlank() }
		}
		val chapters = chapterObjectRegex.findAll(chaptersDoc).mapNotNull { match ->
			val id = match.groupValues[1]
			// Fields order varies between objects: parse each field independently
			// inside the object window (first match wins).
			val body = chaptersDoc.substring(match.range.first, minOf(match.range.first + 1500, chaptersDoc.length))
			val number = numberRegex.find(body)?.groupValues?.get(1)?.toFloatOrNull()
				?: return@mapNotNull null
			val branchId = branchRegex.find(body)?.groupValues?.get(1)
				?: return@mapNotNull null
			val name = nameRegex.find(body)?.groupValues?.get(1)?.unescapeJson()
			val title = titleRegex.find(body)?.groupValues?.get(1)?.unescapeJson()
			val scanlator = publisherRegex.find(body)?.groupValues?.get(1)
				?.unescapeJson()?.takeUnless { it.isBlank() }
			val createdAt = createdRegex.find(body)?.groupValues?.get(1)
			val date = dateRegex.find(body)?.groupValues?.get(1)
			MangaChapter(
				id = generateUid(id),
				url = "/content/$slug/$id",
				title = name.takeUnless { it.isNullOrBlank() } ?: title,
				number = number,
				volume = volumeRegex.find(body)?.groupValues?.get(1)?.toIntOrNull() ?: 0,
				uploadDate = dateFormat.parseSafe(createdAt)
					.takeIf { it != 0L } ?: dateFormatShort.parseSafe(date),
				scanlator = scanlator,
				branch = scanlator ?: branchId,
				source = source,
			)
		}.toList().reversed()
		return manga.copy(
			title = doc.selectFirst("h1")?.text()?.trim()?.takeUnless { it.isEmpty() } ?: manga.title,
			description = description,
			coverUrl = doc.selectFirst("meta[property=og:image]")?.attr("content")?.trim()
				?.takeUnless { it.isEmpty() } ?: manga.coverUrl,
			tags = manga.tags + tags,
			authors = authors,
			chapters = chapters,
		)
	}

	override fun getRequestHeaders() = Headers.Builder()
		.add("User-Agent", UserAgents.CHROME_DESKTOP)
		.build()

	private fun Any?.toSafeInt(): Int {
		return when(this) {
			is Number -> this.toInt()
			is String -> this.toIntOrNull() ?: 0
			else -> 0
		}
	}

	private fun Any?.toSafeFloat(): Float {
		return when(this) {
			is Number -> this.toFloat()
			is String -> this.toFloatOrNull() ?: 0f
			else -> 0f
		}
	}

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
		val chapterId = chapter.url.substringAfterLast('/')
		val json = webClient.httpGet(
			"https://$apiDomain/v2/chapters/$chapterId",
			getRequestHeaders(),
		).parseJson()
		return json.getJSONArray("pages").mapJSON { pageMap ->
			val id = pageMap.getString("id")
			val imageUrl = pageMap.getString("image")
			MangaPage(
				id = generateUid(id),
				url = "$imageUrl?width=1600",
				preview = null,
				source = source,
			)
		}
	}

	private suspend fun fetchAstroData(relativeUrl: String): Map<*, *>? {
		val fullUrl = relativeUrl.toAbsoluteUrl(domain)

		val response = webClient.httpGet(fullUrl)

		val protection = CloudFlareHelper.checkResponseForProtection(response.copy())
		if (protection != CloudFlareHelper.PROTECTION_NOT_DETECTED) {
			response.close()
			context.requestBrowserAction(this, fullUrl)
			return null
		}

		val responseHtml = response.parseHtml()
		val scriptElement = responseHtml.getElementById("it-astro-state")
			?: throw ParseException("Не удалось найти <script id='it-astro-state'> на странице $fullUrl.", fullUrl)

		return astroJsonParser.parse(scriptElement.data())
	}

	override suspend fun getFilterOptions(): MangaListFilterOptions {
		// The HTML catalog supports text search only; tags/states/ratings
		// need the books API which is currently unavailable.
		return MangaListFilterOptions()
	}

	private class AstroJsonParser {
		fun parse(compressedJson: String): Map<*, *>? {
			return try {
				val rootArray = JSONArray(compressedJson)
				if (rootArray.length() == 0) return emptyMap<Any, Any>()

				val cache = mutableMapOf<Int, Any?>()
				val overdueMap = decompress(rootArray.get(0), rootArray, cache) as? Map<*, *> ?: emptyMap<Any, Any>()

				return overdueMap["@inox-tools/request-nanostores"] as? Map<*, *> ?: emptyMap<Any, Any>()
			} catch (e: Exception) {
				e.printStackTrace()
				null
			}
		}

		private fun decompress(value: Any?, rootArray: JSONArray, cache: MutableMap<Int, Any?>): Any? {
			if (value is Int) {
				val ref = value
				if (cache.containsKey(ref)) return cache[ref]
				if (ref < 0 || ref >= rootArray.length()) return ref

				cache[ref] = null
				val referencedItem = rootArray.get(ref)
				val result = processItem(referencedItem, rootArray, cache)
				cache[ref] = result
				return result
			}
			return processItem(value, rootArray, cache)
		}

		private fun processItem(item: Any?, rootArray: JSONArray, cache: MutableMap<Int, Any?>): Any? {
			return when (item) {
				is JSONObject -> {
					val map = mutableMapOf<String, Any?>()
					for (key in item.keys()) {
						map[key] = decompress(item.get(key), rootArray, cache)
					}
					map
				}
				is JSONArray -> {
					if (item.length() > 0 && item.get(0) is String) {
						when (item.getString(0)) {
							"Map" -> {
								val map = mutableMapOf<Any?, Any?>()
								for (i in 1 until item.length() step 2) {
									val key = decompress(item.get(i), rootArray, cache)
									val value = decompress(item.get(i + 1), rootArray, cache)
									if (key != null) map[key] = value
								}
								return map
							}
							"URL" -> return if (item.length() > 1) decompress(item.get(1), rootArray, cache) else null
						}
					}
					(0 until item.length()).map { i -> decompress(item.get(i), rootArray, cache) }
				}
				else -> item
			}
		}
	}
}
