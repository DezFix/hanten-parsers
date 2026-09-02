package hanten.wre.app.parsers.site.iken.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.iken.IkenParser

@MangaSourceParser("MAGUSMANGA", "MagusToon", "en")
internal class MagusToon(context: MangaLoaderContext) :
        IkenParser(context, MangaParserSource.MAGUSMANGA, "magustoon.org", 18, true)
