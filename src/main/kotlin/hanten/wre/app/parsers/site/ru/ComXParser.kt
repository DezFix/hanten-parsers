package hanten.wre.app.parsers.site.ru

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
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
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

@MangaSourceParser("COMX", "Com-X", "ru", ContentType.COMICS)
internal class ComXParser(context: MangaLoaderContext) :
	PagedMangaParser(context, MangaParserSource.COMX, 20),
	Interceptor {

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

	// The site only serves the real content to browser-like requesters:
	// a full UA without Accept-Language gets a JS spinner page instead.
	override fun getRequestHeaders(): Headers = super.getRequestHeaders().newBuilder()
		.add("Accept-Language", "ru,en;q=0.9")
		.build()

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
		val legacy = root.select(".readed .readed__title a[href], li.latest.grid-item a[href$=\".html\"]")
		if (legacy.isNotEmpty()) {
			return legacy.mapNotNull(::parseLegacyLink)
		}
		if (!filter.query.isNullOrEmpty() &&
			root.getElementsContainingOwnText("Ничего не найдено").isNotEmpty()
		) {
			return emptyList()
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
			rating = parseRating(doc) ?: manga.rating,
		)
	}

	// Classic DLE unit-rating: li.current-rating carries width % (rating/5*100)
	// and the 0-5 value as text; "0 голосов" means no votes yet
	private fun parseRating(doc: Document): Float? {
		val votes = doc.selectFirst("div.page__rating-votes")?.text()?.let { text ->
			Regex("(\\d+)").find(text)?.groupValues?.get(1)?.toIntOrNull()
		} ?: 0
		if (votes <= 0) {
			return null
		}
		val li = doc.selectFirst("li.current-rating") ?: return null
		li.text().trim().toFloatOrNull()?.div(5f)?.let { return it.coerceIn(0f, 1f) }
		return Regex("(\\d+(?:\\.\\d+)?)%").find(li.attr("style"))
			?.groupValues?.get(1)?.toFloatOrNull()?.div(100f)?.coerceIn(0f, 1f)
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

	// The site guards content pages with a JS proof-of-work challenge
	// (302 to /_c, SHA-256(token:nonce) starting with "00", POST to /_v).
	// Solve it here so plain OkHttp requests pass without a WebView.
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		if (!request.url.host.endsWith(domain) || isGuardCall(request)) {
			return chain.proceed(request)
		}
		var response = chain.proceed(request)
		// Up to two unlock rounds: the challenge markup varies between
		// renders, so a first attempt may grab a stale token.
		repeat(2) {
			val guard = findGuard(request, response) ?: return response
			response.close()
			try {
				solveGuard(chain, guard)
			} catch (e: Exception) {
				if (e is InterruptedException) {
					Thread.currentThread().interrupt()
				}
			}
			response = chain.proceed(request)
		}
		return response
	}

	private fun isGuardCall(request: okhttp3.Request): Boolean {
		val path = request.url.encodedPath
		return path == "/_c" || path == "/_v"
	}

	private data class Guard(val url: String, val token: String?)

	private fun findGuard(request: okhttp3.Request, response: Response): Guard? {
		if (response.code in 301..308) {
			val location = response.header("Location") ?: return null
			if ("/_c?" in location) {
				val url = request.url.resolve(location)?.toString() ?: return null
				return Guard(url, null)
			}
			return null
		}
		if (response.code == 404) {
			val peek = runCatching { response.peekBody(64 * 1024).string() }.getOrNull()
			if (peek != null && "pow_nonce" in peek) {
				// Redirects are followed, so the challenge URL is the request
				// actually served (the final /_c); the token is re-read below.
				return Guard(response.request.url.toString(), extractToken(peek))
			}
		}
		return null
	}

	private fun extractToken(html: String): String? {
		Regex("""var p\s*=\s*\{\s*token:\s*"([^"]+)"""").find(html)?.let {
			return it.groupValues[1]
		}
		// Fallback: longest token-like match (challenge renders vary,
		// short debug strings may come first).
		return Regex("""token:\s*"([^"]+)"""").findAll(html)
			.map { it.groupValues[1] }
			.filter { it.length >= 40 }
			.maxByOrNull { it.length }
	}

	private fun solveGuard(chain: Interceptor.Chain, guard: Guard) {
		val guardRequest = chain.request().newBuilder().url(guard.url).get().build()
		val guardBody = chain.proceed(guardRequest).use { resp ->
			if (!resp.isSuccessful && resp.code != 404) {
				throw ParseException("Guard check failed: ${resp.code}", guard.url)
			}
			resp.requireBody().string()
		}
		val token = extractToken(guardBody) ?: guard.token
		?: throw ParseException("Guard token not found", guard.url)
		val (nonce, hash) = solvePow(token)
		val form = FormBody.Builder()
			.add("token", token)
			.add("mode", "modern")
			.add("workTime", "380")
			.add("iterations", (nonce + 50).toString())
			.add("hasCrypto", "1")
			.add("pow_nonce", nonce.toString())
			.add("pow_hash", hash)
			.add("webdriver", "0")
			.add("touch", "0")
			.add("screen_w", "1920")
			.add("screen_h", "1080")
			.add("screen_cd", "24")
			.add("tz", "-180")
			.add("dpr", "1")
			.add("cdp", "0")
			.add("cdpf", "")
			.build()
		val verifyUrl = guardRequest.url.newBuilder().encodedPath("/_v").query(null).build()
		val verifyRequest = chain.request().newBuilder()
			.url(verifyUrl)
			.post(form)
			.header("Referer", guard.url)
			.header("Origin", "${guardRequest.url.scheme}://${guardRequest.url.host}")
			.header("X-Requested-With", "XMLHttpRequest")
			.build()
		chain.proceed(verifyRequest).use { resp ->
			if (!resp.isSuccessful) {
				throw ParseException("Guard verification failed: ${resp.code}", verifyUrl.toString())
			}
			resp.requireBody().close()
		}
	}

	private fun solvePow(token: String): Pair<Long, String> {
		val digest = MessageDigest.getInstance("SHA-256")
		val hex = "0123456789abcdef".toCharArray()
		var nonce = 0L
		while (nonce < 5_000_000L) {
			if (Thread.currentThread().isInterrupted) {
				throw InterruptedException()
			}
			val hash = digest.digest("$token:$nonce".toByteArray())
			val sb = StringBuilder(hash.size * 2)
			for (b in hash) {
				val v = b.toInt() and 0xFF
				sb.append(hex[v ushr 4]).append(hex[v and 0x0F])
			}
			if (sb[0] == '0' && sb[1] == '0') {
				return nonce to sb.toString()
			}
			nonce++
			digest.reset()
		}
		throw ParseException("Guard PoW not solved", token.take(16))
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
