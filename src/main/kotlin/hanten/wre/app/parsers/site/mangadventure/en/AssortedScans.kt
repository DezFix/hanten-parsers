package hanten.wre.app.parsers.site.mangadventure.en

import androidx.collection.ArraySet
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterOptions
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangadventure.MangAdventureParser

@MangaSourceParser("ASSORTEDSCANS", "AssortedScans", "en")
internal class AssortedScans(context: MangaLoaderContext) :
	MangAdventureParser(context, MangaParserSource.ASSORTEDSCANS, "assortedscans.com") {
	// tags that don't have any series and make the tests fail
	private val emptyTags = setOf(
		"Doujinshi", "Harem", "Hentai", "Mecha",
		"Shoujo Ai", "Shounen Ai", "Smut", "Yaoi",
	)

	override suspend fun getFilterOptions(): MangaListFilterOptions {
		val options = super.getFilterOptions()
		return options.copy(
			availableTags = options.availableTags.filterNotTo(ArraySet(options.availableTags.size)) { it.key in emptyTags },
		)
	}
}
