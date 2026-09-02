package hanten.wre.app.parsers.site.heancmsalt.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.heancmsalt.HeanCmsAlt
import hanten.wre.app.parsers.Broken

@Broken("Not dead, changed template")
@MangaSourceParser("LEGIONSCANS", "CerberusSeries", "es")
internal class CerberuSeries(context: MangaLoaderContext) :
	HeanCmsAlt(context, MangaParserSource.LEGIONSCANS, "legionscans.com")
