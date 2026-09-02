package hanten.wre.app.parsers.site.mangareader.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaListFilterCapabilities
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.mangareader.MangaReaderParser

@MangaSourceParser("KIRYUU", "Kiryuu", "id")
internal class KiryuuParser(context: MangaLoaderContext) :
	MangaReaderParser(context, MangaParserSource.KIRYUU, "kiryuu02.com", pageSize = 50, searchPageSize = 10) {

	override val listUrl = "/manga/"

	override val filterCapabilities: MangaListFilterCapabilities
		get() = super.filterCapabilities.copy(
			isTagsExclusionSupported = false,
		)
}
