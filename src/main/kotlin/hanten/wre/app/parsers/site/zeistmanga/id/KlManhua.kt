package hanten.wre.app.parsers.site.zeistmanga.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaTag
import hanten.wre.app.parsers.site.zeistmanga.ZeistMangaParser
import hanten.wre.app.parsers.util.mapToSet
import hanten.wre.app.parsers.util.parseHtml
import hanten.wre.app.parsers.util.requireElementById

@MangaSourceParser("KLMANHUA", "KlManhua", "id", ContentType.HENTAI)
internal class KlManhua(context: MangaLoaderContext) :
	ZeistMangaParser(context, MangaParserSource.KLMANHUA, "klmanhua.blogspot.com") {

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
