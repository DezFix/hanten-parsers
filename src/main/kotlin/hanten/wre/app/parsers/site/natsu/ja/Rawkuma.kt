package hanten.wre.app.parsers.site.natsu.ja

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.natsu.NatsuParser

@MangaSourceParser("RAWKUMA", "Rawkuma", "ja")
internal class Rawkuma(context: MangaLoaderContext) :
	NatsuParser(context, MangaParserSource.RAWKUMA, pageSize = 24) {

	override val configKeyDomain = ConfigKey.Domain("rawkuma.net")
}
