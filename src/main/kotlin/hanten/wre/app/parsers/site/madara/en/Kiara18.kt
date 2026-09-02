package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.Broken
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.ContentType
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@Broken
@MangaSourceParser("KIARA18", "Kiara18", "en", ContentType.HENTAI)
internal class Kiara18(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.KIARA18, "18.kiara.cool")
