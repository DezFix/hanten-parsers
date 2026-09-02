package hanten.wre.app.parsers.site.gallery.vi

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.site.gallery.GalleryParser

@MangaSourceParser("BUONDUA", "Buon Dua", "vi", type = ContentType.OTHER)
internal class BuonDua(context: MangaLoaderContext) :
    GalleryParser(context, MangaParserSource.BUONDUA, "buondua.com") {

    override val configKeyDomain: ConfigKey.Domain = ConfigKey.Domain(
        "buondua.com",
        "buondua.us",
    )
}