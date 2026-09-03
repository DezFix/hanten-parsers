package hanten.wre.app.site.natsu.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.natsu.NatsuParser

@MangaSourceParser("IKIRU", "Ikiru", "id")
internal class Ikiru(context: MangaLoaderContext) :
    NatsuParser(context, MangaParserSource.IKIRU, pageSize = 24) {

    override val configKeyDomain = ConfigKey.Domain("06.ikiru.wtf")

    override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
        super.onCreateConfig(keys)
        keys.add(userAgentKey)
    }
}
