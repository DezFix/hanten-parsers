package hanten.wre.app.parsers.site.vmp.es

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.vmp.VmpParser

// Other domain name : toonx.net
@MangaSourceParser("VERCOMICSPORNO", "VerComicsPorno", "es", ContentType.HENTAI)
internal class VerComicsPorno(context: MangaLoaderContext) :
	VmpParser(context, MangaParserSource.VERCOMICSPORNO, "vercomicsporno.com") {
	override val listUrl = "comics-porno/"
	override val geneUrl = "etiquetas/"
}
