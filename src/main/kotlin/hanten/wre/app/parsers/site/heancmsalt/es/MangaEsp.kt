package hanten.wre.app.parsers.site.heancmsalt.es

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.heancmsalt.HeanCmsAlt

@Broken("Not dead, changed template")
@MangaSourceParser("MANGAESP", "MangaEsp", "es")
internal class MangaEsp(context: MangaLoaderContext) :
	HeanCmsAlt(context, MangaParserSource.MANGAESP, "mangaesp.topmanhuas.org", 15) {

	override val listUrl = "/comic"

	override val selectManga = "div.contenedor div.grid-5  .p-relative:not(.portada-contenedor)"
	override val selectMangaTitle = "div.titulo-contenedor"

	override val selectDesc = "div.project-sinopsis-contenido"
	override val selectAlt = "div.project-info-opcion:contains(Altenativo) div.project-info-contenido"
	override val selectChapter = "div.grid-capitulos div a"
	override val selectChapterTitle = ".capitulo-info-titulo"
	override val selectChapterDate = ".capitulo-info-fecha"

	override val selectPage = ".grid-center img"
}
