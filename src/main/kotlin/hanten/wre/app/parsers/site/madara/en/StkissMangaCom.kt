package hanten.wre.app.parsers.site.madara.en

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import hanten.wre.app.parsers.Broken

@Broken("Original site closed")
@MangaSourceParser("STKISSMANGA_COM", "1stKissManga.com", "en")
internal class StkissMangaCom(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.STKISSMANGA_COM, "1stkissmanga.mom", 10) {
            override val postReq = true
      }
