package hanten.wre.app.parsers.site.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.uzaymanga.UzayMangaParser

@MangaSourceParser("TENSHIMANGA", "Tenshi Manga", "tr")
internal class TenshiManga(context: MangaLoaderContext) :
	UzayMangaParser(
		context = context,
		source = MangaParserSource.TENSHIMANGA,
		domain = "tenshimanga.com",
		cdnUrl = "https://tenshimangacdn4.efsaneler.can.re",
	)
