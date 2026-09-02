package hanten.wre.app.parsers

import hanten.wre.app.parsers.config.ConfigKey
import hanten.wre.app.parsers.config.MangaSourceConfig

internal class SourceConfigMock : MangaSourceConfig {

	override fun <T> get(key: ConfigKey<T>): T = key.defaultValue
}