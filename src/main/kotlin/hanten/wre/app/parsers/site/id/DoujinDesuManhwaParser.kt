package hanten.wre.app.parsers.site.id

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import java.util.*

@MangaSourceParser("DOUJINDESU_MANHWA", "DoujinDesu Manhwa", "id")
internal class DoujinDesuManhwaParser(context: MangaLoaderContext) :
	BaseDoujinDesuParser(context, MangaParserSource.DOUJINDESU_MANHWA) {

	override val defaultTypes: String = "manhwa"

	override val availableContentTypes: Set<ContentType> = EnumSet.of(ContentType.MANHWA)
}
