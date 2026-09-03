package hanten.wre.app.parsers.site.madara.id

import okhttp3.Headers
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.MangaSourceParser
import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.site.madara.MadaraParser
import java.util.*

@MangaSourceParser("MGKOMIK", "MgKomik", "id")
internal class Mgkomik(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.MGKOMIK, "id.mgkomik.cc", 20) {

	override fun onCreateConfig(keys: MutableCollection<ConfigKey<*>>) {
		super.onCreateConfig(keys)
		keys.add(ConfigKey.InterceptCloudflare(defaultValue = true))
	}

	override val tagPrefix = "genres/"
	override val listUrl = "komik/"
	override val datePattern = "dd MMM yy"
	override val stylePage = ""
	override val sourceLocale: Locale = Locale.ENGLISH
	override val withoutAjax = true
	override val postReq = true

	override fun getRequestHeaders(): Headers = super.getRequestHeaders().newBuilder()
		.add("Accept-Language", "id-ID,id;q=0.9,en-US;q=0.8,en;q=0.7")
		.add("Referer", "https://$domain/")
		.build()
}
