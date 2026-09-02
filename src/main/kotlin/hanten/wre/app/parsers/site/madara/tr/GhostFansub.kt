package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken("Redirect to @GRIMELEK")
@MangaSourceParser("GHOSTFANSUB", "GhostFansub", "tr")
internal class GhostFansub(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.GHOSTFANSUB, "ghostfansub.co", 18)
// you now need to log in to access content
