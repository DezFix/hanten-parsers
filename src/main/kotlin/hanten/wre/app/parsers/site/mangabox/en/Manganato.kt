package hanten.wre.app.parsers.site.mangabox.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangabox.MangaboxParser
import hanten.wre.app.parsers.Broken

@Broken
@MangaSourceParser("MANGANATO", "Manganato", "en")
internal class Manganato(context: MangaLoaderContext) :
	MangaboxParser(context, MangaParserSource.MANGANATO) {
	override val configKeyDomain = ConfigKey.Domain(
		"natomanga.com",
		"manganato.gg",
	)
	override val otherDomain = "manganato.gg"

	override val authorUrl = "/author/story"
	override val selectPage = ".container-chapter-reader > img"
}
