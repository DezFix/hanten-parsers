package hanten.wre.app.parsers.site.madara.es

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("RAGNAROKSCAN", "RagnarokScan", "es")
internal class RagnarokScan(context: MangaLoaderContext) :
    MadaraParser(context, MangaParserSource.RAGNAROKSCAN, "ragnarokscan.com") {
    override val stylePage = ""
    override val listUrl = "series/"
    override val tagPrefix = "genero/"
}
