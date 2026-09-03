package hanten.wre.app.parsers.site.mangahub.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangahub.MangaHubParser

@Broken("BLOCKED BY CLOUDFARE")

@MangaSourceParser("MANGAHUB_IO", "MangaHubIO", "en")
internal class MangaHubIo(context: MangaLoaderContext) :
	MangaHubParser(context, MangaParserSource.MANGAHUB_IO, "mangahub.io", "m01") {
    override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
        super.onCreateConfig(keys)
        keys.add(ConfigKey.InterceptCloudflare(defaultValue = true))
    }
}
