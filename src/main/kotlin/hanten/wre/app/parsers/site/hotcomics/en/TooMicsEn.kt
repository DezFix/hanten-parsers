package hanten.wre.app.parsers.site.hotcomics.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.hotcomics.HotComicsParser

@MangaSourceParser("TOOMICSEN", "TooMicsEn", "en")
internal class TooMicsEn(context: MangaLoaderContext) :
	HotComicsParser(context, MangaParserSource.TOOMICSEN, "toomics.com/en") {
	override val isSearchSupported = false
	override val mangasUrl = "/webtoon/ranking/genre"
	override val selectMangas = "li > div.visual"
	override val selectMangaChapters = "li.normal_ep:has(.coin-type1)"
	override val selectTagsList = "div.genre_list li:not(.on) a"
	override val selectPages = "div[id^=load_image_] img"
	override val onePage = true
}
