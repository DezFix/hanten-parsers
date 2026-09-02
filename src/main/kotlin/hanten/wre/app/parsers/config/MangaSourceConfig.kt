package hanten.wre.app.parsers.config

public interface MangaSourceConfig {

	public operator fun <T> get(key: ConfigKey<T>): T
}
