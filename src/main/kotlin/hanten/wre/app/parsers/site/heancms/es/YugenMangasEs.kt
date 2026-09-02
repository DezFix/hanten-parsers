package hanten.wre.app.parsers.site.heancms.es

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.*
import hanten.wre.app.parsers.site.heancms.HeanCms

@Broken("Not dead, changed template")
@MangaSourceParser("YUGEN_MANGAS_ES", "YugenMangas.lat", "es", ContentType.HENTAI)
internal class YugenMangasEs(context: MangaLoaderContext) :
	HeanCms(context, MangaParserSource.YUGEN_MANGAS_ES, "lectorikigai.acamu.net")
