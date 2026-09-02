package hanten.wre.app.parsers.site.madtheme.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madtheme.MadthemeParser

@MangaSourceParser("TOONITUBE", "TooniTube", "en", ContentType.HENTAI)
internal class TooniTube(context: MangaLoaderContext) :
	MadthemeParser(context, MangaParserSource.TOONITUBE, "toonitube.com") {
	override val selectDesc = "div.summary div.section-body p.content"
}
