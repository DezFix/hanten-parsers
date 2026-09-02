package hanten.wre.app.parsers.site.madara.tr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("SARCASMSCANS", "SarcasmScans", "tr", ContentType.HENTAI)
internal class SarcasmScans(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SARCASMSCANS, "sarcasmscans.com", 16)
