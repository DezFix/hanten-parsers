package hanten.wre.app.parsers.exception

import okio.IOException
import org.json.JSONArray
import hanten.wre.app.parsers.InternalParsersApi
import hanten.wre.app.parsers.util.json.mapJSONNotNull

public class GraphQLException @InternalParsersApi constructor(errors: JSONArray) : IOException() {

	public val messages: List<String> = errors.mapJSONNotNull {
		it.getString("message")
	}

	override val message: String
		get() = messages.joinToString("\n")
}
