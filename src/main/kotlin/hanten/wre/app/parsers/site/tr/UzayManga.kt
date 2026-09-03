package hanten.wre.app.parsers.site.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.uzaymanga.UzayMangaParser

@Broken
@MangaSourceParser("UZAYMANGA", "Uzay Manga", "tr")
internal class UzayManga(context: MangaLoaderContext) :
	UzayMangaParser(
		context = context,
		source = MangaParserSource.UZAYMANGA,
		domain = "uzaymanga.com",
		cdnUrl = "https://uzaymangacdn3.efsaneler.can.re",
	)
