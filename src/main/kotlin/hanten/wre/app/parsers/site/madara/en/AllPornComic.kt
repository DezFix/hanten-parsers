package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("ALLPORN_COMIC", "AllPornComic", "en", ContentType.HENTAI)
internal class AllPornComic(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.ALLPORN_COMIC, "allporncomic.com", pageSize = 24) {
	override val tagPrefix = "porncomic-genre/"
	override val datePattern = "MMMM dd, yyyy"
}
