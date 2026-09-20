package hanten.wre.app.parsers.site.ru

import androidx.collection.ArrayMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jsoup.nodes.Document
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.core.PagedMangaParser
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.util.*
import java.util.*

@MangaSourceParser("ACOMICS", "AComics", "ru", ContentType.COMICS)
internal class AComics(context: MangaLoaderContext) :
    PagedMangaParser(context, MangaParserSource.ACOMICS, pageSize = 10) {

    override val availableSortOrders: Set<SortOrder> = EnumSet.of(
        SortOrder.UPDATED,
        SortOrder.ALPHABETICAL,
        SortOrder.POPULARITY,
    )

    override val configKeyDomain = ConfigKey.Domain("acomics.ru")

    override val filterCapabilities: MangaListFilterCapabilities
        get() = MangaListFilterCapabilities(
            isMultipleTagsSupported = true,
            isSearchSupported = true,
        )

    init {
        paginator.firstPage = 0
        searchPaginator.firstPage = 0
        context.cookieJar.insertCookies(domain, "ageRestrict=18")
    }

    override suspend fun getFilterOptions() = MangaListFilterOptions(
        availableTags = getOrCreateTagMap().byKey.values.toSet(),
        availableStates = EnumSet.of(MangaState.ONGOING, MangaState.FINISHED),
    )

    override suspend fun getListPage(
        page: Int,
        order: SortOrder,
        filter: MangaListFilter,
    ): List<Manga> {
        val url = buildString {
            append("https://")
            append(domain)
            when {
                !filter.query.isNullOrEmpty() -> {
                    if (page > 0) {
                        return emptyList()
                    }
                    append("/search?keyword=")
                    append(filter.query)
                }

                else -> {
                    append("/comics?ratings[]=1&ratings[]=2&ratings[]=3&ratings[]=4&ratings[]=5&ratings[]=6&skip=")
                    append(page * 10)
                    append("&sort=")
                    append(
                        when (order) {
                            SortOrder.UPDATED -> "last_update"
                            SortOrder.ALPHABETICAL -> "serial_name"
                            SortOrder.POPULARITY -> "subscr_count"
                            else -> "last_update"
                        },
                    )

                    if (filter.tags.isNotEmpty()) {
                        append("&categories=")
                        append(filter.tags.joinToString(separator = ",") { it.key })
                    }

                    if (filter.states.isNotEmpty()) {
                        append("&updatable=")
                        append(
                            filter.states.oneOrThrowIfMany().let {
                                when (it) {
                                    MangaState.ONGOING -> "yes"
                                    MangaState.FINISHED -> "no"
                                    else -> "0"
                                }
                            },
                        )
                    }
                }
            }
        }

        return parseMangaList(webClient.httpGet(url).parseHtml())
    }

    private fun parseMangaList(docs: Document): List<Manga> {
        return docs.select("section.serial-card").mapNotNull { card ->
            val a = card.selectFirst("h2.title a[href]") ?: return@mapNotNull null
            val href = a.attrAsRelativeUrl("href")
            val url = href.toAbsoluteUrl(domain) + "/about"
            val title = a.text().trim().takeUnless { it.isEmpty() } ?: return@mapNotNull null
            val cover = card.selectFirst("a.cover img")?.let { img ->
                img.attr("data-real-src").takeUnless { it.isEmpty() } ?: img.src()
            }.orEmpty().takeUnless { it.isEmpty() }?.toAbsoluteUrl(domain).orEmpty()
            Manga(
                id = generateUid(url),
                url = url,
                title = title,
                altTitles = emptySet(),
                publicUrl = url,
                rating = RATING_UNKNOWN,
                contentRating = if (isNsfwSource) ContentRating.ADULT else null,
                coverUrl = cover,
                tags = emptySet(),
                state = null,
                authors = emptySet(),
                source = source,
            )
        }
    }

    private class TagMaps(
        val byKey: Map<String, MangaTag>,
        val bySlug: Map<String, MangaTag>,
    )

    private var tagCache: TagMaps? = null
    private val mutex = Mutex()

    private suspend fun getOrCreateTagMap(): TagMaps = mutex.withLock {
        tagCache?.let { return@withLock it }
        val doc = webClient.httpGet("https://$domain/comics").parseHtml()
        val byKey = ArrayMap<String, MangaTag>()
        val bySlug = ArrayMap<String, MangaTag>()
        doc.select("form.catalog-filters-form fieldset.categories label").forEach { label ->
            val key = label.selectFirst("input[value]")?.attr("value")?.takeUnless { it.isEmpty() }
                ?: return@forEach
            val title = label.text().trim().takeUnless { it.isEmpty() } ?: return@forEach
            val slug = label.classNames().firstOrNull { it.startsWith("category-") }
                ?.removePrefix("category-")?.takeUnless { it.isEmpty() }
            val tag = MangaTag(title = title, key = key, source = source)
            byKey[key] = tag
            if (slug != null) {
                bySlug[slug] = tag
            }
        }
        return@withLock TagMaps(byKey, bySlug).also { tagCache = it }
    }

    override suspend fun getDetails(manga: Manga): Manga {
        val doc = webClient.httpGet(manga.url.toAbsoluteUrl(domain)).parseHtml()
        val tagMap = getOrCreateTagMap()
        val tags = doc.select("p.serial-about-badges a.badge.category").mapNotNullToSet { a ->
            val slug = a.attr("href").removeSuffix("/").substringAfterLast("/")
                .takeUnless { it.isEmpty() } ?: return@mapNotNullToSet null
            tagMap.bySlug[slug]
        }
        val authors = doc.select("p.serial-about-authors a").eachText().toSet()
        return manga.copy(
            tags = tags,
            description = doc.selectFirst("section.serial-about-text")?.text()?.trim(),
            authors = authors,
            chapters = listOf(
                MangaChapter(
                    id = manga.id,
                    title = manga.title,
                    number = 1f,
                    volume = 0,
                    url = manga.url.replace("/about", "/"),
                    scanlator = null,
                    uploadDate = 0,
                    branch = null,
                    source = source,
                ),
            ),
        )
    }

    override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
        val doc = webClient.httpGet(chapter.url + "1").parseHtml()
        val totalPages = doc.selectFirst("h1.reader-issue-title span.number")?.text()
            ?.substringAfterLast('/', "")?.trim()?.toIntOrNull()
            ?: doc.selectFirst("nav.reader-navigator[data-issue-count]")
                ?.attr("data-issue-count")?.toIntOrNull()
            ?: throw ParseException("Cannot determine pages count", chapter.url)
        return (1..totalPages).map {
            val url = chapter.url + it
            MangaPage(
                id = generateUid(url),
                url = url,
                preview = null,
                source = source,
            )
        }
    }

    override suspend fun getPageUrl(page: MangaPage): String {
        val doc = webClient.httpGet(page.url.toAbsoluteUrl(domain)).parseHtml()
        return doc.selectFirstOrThrow("section.reader-issue img.issue").src()
            ?: throw ParseException("Image not found", page.url)
    }
}
