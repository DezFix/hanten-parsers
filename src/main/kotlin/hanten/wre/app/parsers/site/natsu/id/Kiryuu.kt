package hanten.wre.app.parsers.site.natsu.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.natsu.NatsuParser
import hanten.wre.app.parsers.util.generateUid
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.requireSrc
import hanten.wre.app.parsers.util.toAbsoluteUrl
import hanten.wre.app.parsers.util.toRelativeUrl

@MangaSourceParser("KIRYUU", "Kiryuu", "id")
internal class Kiryuu(context: MangaLoaderContext) :
    NatsuParser(context, MangaParserSource.KIRYUU, 24) {
    override val configKeyDomain = ConfigKey.Domain("v6.kiryuu.to")

    override suspend fun getPages(chapter: MangaChapter): List<MangaPage> {
        val doc = webClient.httpGet(chapter.url.toAbsoluteUrl(domain)).parseHtml()
        // Images are in a section with data-image-data attribute
        return doc.select("section[data-image-data] img").map { img ->
            val url = img.requireSrc().toRelativeUrl(domain)
            MangaPage(
                id = generateUid(url),
                url = url,
                preview = null,
                source = source,
            )
        }
    }
}
