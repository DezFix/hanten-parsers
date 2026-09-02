package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("READMANHUA", "ReadManhua (Broken)", "en", ContentType.HENTAI)
internal class ReadManhua(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.READMANHUA, "readmanhua.net", 20) {
	override val postReq = true
	override val datePattern = "d MMM yy"
}
