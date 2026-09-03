package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.initmanga.InitMangaParser

@MangaSourceParser("MERLINSCANS", "MerlinScans", "tr")
internal class MerlinScans(context: MangaLoaderContext) :
	InitMangaParser(
		context = context,
		source = MangaParserSource.MERLINSCANS,
		domain = "merlintoon.com",
		pageSize = 20,
		searchPageSize = 20,
		latestUrlSlug = "son-guncellenenler",
	)
