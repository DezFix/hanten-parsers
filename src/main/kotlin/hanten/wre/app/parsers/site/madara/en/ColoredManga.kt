package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("COLORED_MANGA", "ColoredManga", "en")
internal class ColoredManga(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.COLORED_MANGA, "coloredmanga.net") {
	override val datePattern = "dd-MMM"
}
