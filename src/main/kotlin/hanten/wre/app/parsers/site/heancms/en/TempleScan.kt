package hanten.wre.app.parsers.site.heancms.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.heancms.HeanCms

@Broken("Not dead, changed template")
@MangaSourceParser("TEMPLESCAN", "TempleScan", "en")
internal class TempleScan(context: MangaLoaderContext) :
	HeanCms(context, MangaParserSource.TEMPLESCAN, "templetoons.com") {
	override val pathManga = "comic"

	override val apiPath: String
		get() = "$domain/api"
}
