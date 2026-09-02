package hanten.wre.app.parsers.core

import hanten.wre.app.parsers.InternalParsersApi
import hanten.wre.app.parsers.MangaLoaderContext
import hanten.wre.app.parsers.model.Manga
import hanten.wre.app.parsers.model.MangaListFilter
import hanten.wre.app.parsers.model.MangaParserSource
import hanten.wre.app.parsers.model.SortOrder

@InternalParsersApi
public abstract class SinglePageMangaParser(
	context: MangaLoaderContext,
	source: MangaParserSource,
) : AbstractMangaParser(context, source) {

	final override suspend fun getList(offset: Int, order: SortOrder, filter: MangaListFilter): List<Manga> {
		if (offset > 0) {
			return emptyList()
		}
		return getList(order, filter)
	}

	public abstract suspend fun getList(order: SortOrder, filter: MangaListFilter): List<Manga>
}
