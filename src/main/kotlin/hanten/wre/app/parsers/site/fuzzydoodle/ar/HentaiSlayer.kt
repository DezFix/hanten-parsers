package hanten.wre.app.parsers.site.fuzzydoodle.ar

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaState
import hanten.wre.app.parsers.site.fuzzydoodle.FuzzyDoodleParser
import java.util.*

@MangaSourceParser("HENTAISLAYER", "HentaiSlayer", "ar", ContentType.HENTAI)
internal class HentaiSlayer(context: MangaLoaderContext) :
	FuzzyDoodleParser(context, MangaParserSource.HENTAISLAYER, "hentaislayer.net") {

	override val ongoingValue = "مستمر"
	override val finishedValue = "مكتمل"
	override val abandonedValue = "متوقف"

	override val mangaValue = "مانجا"
	override val manhuaValue = "مانهوا"
	override val comicsValue = "كوميكس"

	override suspend fun getFilterOptions() = super.getFilterOptions().copy(
		availableStates = EnumSet.of(MangaState.ONGOING, MangaState.FINISHED, MangaState.ABANDONED),
		availableContentTypes = EnumSet.of(
			ContentType.MANGA,
			ContentType.MANHUA,
			ContentType.COMICS,
		),
	)
}
