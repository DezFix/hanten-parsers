package hanten.wre.app.parsers.site.mangareader.fr

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser
import hanten.wre.app.parsers.util.insertCookies

@Broken
@MangaSourceParser("ETHERALRADIANCE", "EtheralRadiance", "fr")
internal class EtheralRadiance(context: MangaLoaderContext) :
	MangaReaderParser(
		context,
		MangaParserSource.ETHERALRADIANCE,
		"www.etheralradiance.eu",
		pageSize = 20,
		searchPageSize = 10,
	) {

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)

	init {
		context.cookieJar.insertCookies(
			domain,
			"_lscache_vary=1;",
		)
	}
}
