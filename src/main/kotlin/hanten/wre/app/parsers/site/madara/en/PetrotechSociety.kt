package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("PETROTECHSOCIETY", "Petrotech Society", "en", ContentType.HENTAI)
internal class PetrotechSociety(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.PETROTECHSOCIETY, "www.petrotechsociety.org", pageSize = 10) {
	override val postReq = true
}
