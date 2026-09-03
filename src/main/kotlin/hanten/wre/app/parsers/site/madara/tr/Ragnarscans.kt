package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.initmanga.InitMangaParser

@MangaSourceParser("RAGNARSCANS", "Ragnarscans", "tr")
internal class Ragnarscans(context: MangaLoaderContext) :
	InitMangaParser(
		context = context,
		source = MangaParserSource.RAGNARSCANS,
		domain = "ragnarscans.com",
		pageSize = 20,
		searchPageSize = 20,
		mangaUrlDirectory = "manga",
		popularUrlSlug = "en-cok-takip-edilenler",
		isCloudflareProtected = true,
	)
