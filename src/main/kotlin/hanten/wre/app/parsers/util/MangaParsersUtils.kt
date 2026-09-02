@file:JvmName("MangaParsersUtils")

package hanten.wre.app.parsers.util

import hanten.wre.app.parsers.model.MangaChapter
import hanten.wre.app.parsers.model.MangaListFilter
import kotlin.contracts.contract

public fun MangaListFilter?.isNullOrEmpty(): Boolean {
	contract {
		returns(false) implies (this@isNullOrEmpty != null)
	}
	return this == null || this.isEmpty()
}

public fun Collection<MangaChapter>.findById(chapterId: Long): MangaChapter? = find { x ->
	x.id == chapterId
}
