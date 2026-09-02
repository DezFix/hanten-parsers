package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("SLEEPYTRANSLATIONS", "Sleepy Translations", "en")
internal class SleepyTranslations(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.SLEEPYTRANSLATIONS, "sleepytranslations.com", 16) {
	override val listUrl = "series/"
	override val tagPrefix = "genre/"
}
