package hanten.wre.app.parsers.site.madara.pt

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser

@MangaSourceParser("BORUTOEXPLORER", "BorutoExplorer", "pt")
internal class BorutoExplorer(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.BORUTOEXPLORER, "leitor.borutoexplorer.com.br", 10) {
	override val datePattern: String = "dd 'de' MMMMM 'de' yyyy"
}
