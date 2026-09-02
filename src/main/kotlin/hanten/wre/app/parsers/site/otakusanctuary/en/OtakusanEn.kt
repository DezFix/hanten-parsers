package hanten.wre.app.parsers.site.otakusanctuary.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.otakusanctuary.OtakuSanctuaryParser
import hanten.wre.app.parsers.Broken

@Broken("Original site closed")
@MangaSourceParser("OTAKUSAN_EN", "Otaku Sanctuary (EN)", "en")
internal class OtakusanEn(context: MangaLoaderContext) :
	OtakuSanctuaryParser(context, MangaParserSource.OTAKUSAN_EN, "otakusan.me") {
	override val lang = "us"
	override val selectState = ".table-info tr:contains(Status) td"
}
