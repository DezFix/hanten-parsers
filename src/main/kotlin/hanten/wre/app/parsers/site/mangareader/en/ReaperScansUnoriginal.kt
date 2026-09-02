package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken("Domain offline - could not resolve host")
@MangaSourceParser("REAPERSCANSUNORIGINAL", "ReaperScansUnoriginal", "en")
internal class ReaperScansUnoriginal(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.REAPERSCANSUNORIGINAL,
		"reaper-scans.com",
		pageSize = 30,
		searchPageSize = 42,
	)
