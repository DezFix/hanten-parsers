package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@Broken("Redirect to @FLIXSCANS")
@MangaSourceParser("SNOWSCANS", "SnowScans", "en")
internal class SnowScans(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.SNOWSCANS, "flixscans.net", pageSize = 20, searchPageSize = 10) {
	override val listUrl = "/series"
}
