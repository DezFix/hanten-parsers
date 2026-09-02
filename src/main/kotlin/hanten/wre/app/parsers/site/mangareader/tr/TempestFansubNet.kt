package hanten.wre.app.parsers.site.mangareader.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("TEMPESTFANSUBNET", "tempestmangas.com", "tr")
internal class TempestFansubNet(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.TEMPESTFANSUBNET,
		"tempestfansub.net",
		pageSize = 30,
		searchPageSize = 10,
	)
