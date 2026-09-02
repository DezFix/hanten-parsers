package hanten.wre.app.parsers.site.nepnep.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.nepnep.NepnepParser

// site closed in favour of weeb central
@Broken
@MangaSourceParser("MANGASEE", "MangaSee", "en")
internal class MangaSee(context: MangaLoaderContext) :
	NepnepParser(context, MangaParserSource.MANGASEE, "mangasee123.com")
