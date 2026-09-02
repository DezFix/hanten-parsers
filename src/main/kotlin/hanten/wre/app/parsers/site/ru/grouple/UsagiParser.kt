package hanten.wre.app.parsers.site.ru.grouple

import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource

@MangaSourceParser("USAGI", "Usagi", "ru")
internal class UsagiParser(
	context: MangaLoaderContext,
) : GroupleParser(context, MangaParserSource.USAGI, 1) {

	override val configKeyDomain = ConfigKey.Domain(*domains)

	override fun getRequestHeaders() = super.getRequestHeaders().newBuilder()
		.add("referer", "https://$domain/")
		.build()

	companion object {

		val domains = arrayOf("web.usagi.one")
	}
}
