package hanten.wre.app.parsers.site.gallery.zh

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaListFilterOptions
import hanten.wre.app.parsers.site.gallery.GalleryParser

@MangaSourceParser("XIUTAKU", "Xiutaku", "zh", type = ContentType.OTHER)
internal class Xiutaku(context: MangaLoaderContext) :
    GalleryParser(context, MangaParserSource.XIUTAKU, "xiutaku.com") {
    
    override suspend fun getFilterOptions():
        MangaListFilterOptions = MangaListFilterOptions(availableTags = fetchTags())

}