package hanten.wre.app.parsers.exception

import okio.IOException
import hanten.wre.app.parsers.InternalParsersApi
import hanten.wre.app.parsers.model.MangaSource

/**
 * Authorization is required for access to the requested content
 */
public class AuthRequiredException @InternalParsersApi @JvmOverloads constructor(
	public val source: MangaSource,
	cause: Throwable? = null,
) : IOException("Authorization required", cause)
