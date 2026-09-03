package hanten.wre.app.parsers.site.mangareader.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("SNOWMACHINETRANSLATION", "Snow Machine Translation", "en")

internal class SnowMachineTranslation(context: MangaLoaderContext) :
    MangaReaderParser(context, MangaParserSource.SNOWMACHINETRANSLATION, "snowmachinetranslation.com", pageSize = 24, searchPageSize = 10) {
    override val listUrl = "/manga"


}
