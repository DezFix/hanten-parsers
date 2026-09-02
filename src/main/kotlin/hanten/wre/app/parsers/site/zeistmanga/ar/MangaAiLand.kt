package hanten.wre.app.parsers.site.zeistmanga.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaTag
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser
import hanten.wre.app.parsers.util.mapToSet
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.requireElementById

@MangaSourceParser("MANGAAILAND", "MangaAiLand", "ar")
internal class MangaAiLand(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.MANGAAILAND, "manga-ai-land.blogspot.com") {
	override val sateOngoing: String = "مستمر"
	override val sateFinished: String = "مكتملة"
	override val sateAbandoned: String = "متوقفة"

	override suspend fun fetchAvailableTags(): Set<MangaTag> {
		val doc = webClient.httpGet("https://$domain").parseHtml()
		return doc.requireElementById("LinkList1").select("ul li a").mapToSet {
			MangaTag(
				key = it.attr("href").substringBefore("?").substringAfterLast('/'),
				title = it.text(),
				source = source,
			)
		}
	}
}
