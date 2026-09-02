package hanten.wre.app.parsers.site.ru.rulib

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.exception.AuthRequiredException
import hanten.wre.app.parsers.exception.NotFoundException
import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaPage
import hanten.wre.app.parsers.model.MangaParserSource

@MangaSourceParser("MANGALIB", "MangaLib", "ru")
internal class MangaLibParser(
	context: MangaLoaderContext,
) : LibSocialParser(
	context = context,
	source = MangaParserSource.MANGALIB,
	siteId = 1,
	siteDomains = arrayOf("mangalib.org", "mangalib.me"),
) {

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> = try {
		super.getPages(chapter)
	} catch (e: NotFoundException) {
		throw AuthRequiredException(source, e)
	}
}
