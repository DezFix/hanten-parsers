package hanten.wre.app.parsers.site.fuzzydoodle.fr

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.MangaState
import hanten.wre.app.parsers.site.fuzzydoodle.FuzzyDoodleParser
import java.util.*

@MangaSourceParser("LELSCANVF", "LelScanFr", "fr")
internal class LelScanVf(context: MangaLoaderContext) :
	FuzzyDoodleParser(context, MangaParserSource.LELSCANVF, "lelscanfr.com") {

	override val ongoingValue = "en-cours"
	override val finishedValue = "termin"

	override suspend fun getFilterOptions() = super.getFilterOptions().copy(
		availableStates = EnumSet.of(MangaState.ONGOING, MangaState.FINISHED),
	)
}
