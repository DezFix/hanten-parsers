package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("MANHUAZONGHE", "ManhuaZonghe", "en")
internal class ManhuaZonghe(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MANHUAZONGHE, "www.manhuazonghe.com") {
	override val tagPrefix = "genre/"
	override val listUrl = "manhua/"
}
