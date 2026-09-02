package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.exception.ParseException
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.network.UserAgents
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.util.*

@MangaSourceParser("MADARADEX", "MadaraDex", "en", ContentType.HENTAI)
internal class MadaraDex(context: MangaLoaderContext) :
    MadaraParser(context, MangaParserSource.MADARADEX, "madaradex.org") {

    init {
        context.cookieJar.insertCookies(domain, "wpmanga-adault=1")
    }

    override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
        super.onCreateConfig(keys)
        keys.remove(userAgentKey)
    }

    override fun getRequestHeaders() = super.getRequestHeaders().newBuilder()
        .set("User-Agent", UserAgents.CHROME_DESKTOP)
        .set("referer", "https://madaradex.org/")
        .build()

    override val authUrl: String
        get() = "https://${domain}"

    override suspend fun isAuthorized(): Boolean {
        return context.cookieJar.getCookies(domain).any {
            it.name.contains("cm_uaid")
        }
    }

    override val listUrl = "title/"
    override val tagPrefix = "genre/"
    override val postReq = true

    override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
        val fullUrl = chapter.url.toAbsoluteUrl(domain)
        val doc = webClient.httpGet(fullUrl).parseHtml()
        
        val images = doc.select("div.page-break img")
        
        if (images.isEmpty()) {
            throw ParseException("No images found, try to log in", fullUrl)
        }
        
        return images.mapNotNull { img ->
            val rawUrl = img.attr("data-src").ifBlank { img.attr("src") }.trim()
            
            if (rawUrl.isEmpty()) {
                return@mapNotNull null
            }
            
            val cleanUrl = rawUrl.toRelativeUrl(domain).substringBefore('#')
            
            MangaPage(
                id = generateUid(cleanUrl),
                url = cleanUrl,
                preview = null,
                source = source,
            )
        }
    }
}
