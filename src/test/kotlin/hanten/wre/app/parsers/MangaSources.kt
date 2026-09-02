package hanten.wre.app.parsers

import org.junit.jupiter.params.provider.EnumSource
import hanten.wre.app.parsers.model.MangaParserSource

// Change 'names' to test specified parsers
@EnumSource(MangaParserSource::class, names = [], mode = EnumSource.Mode.INCLUDE)
internal annotation class MangaSources
