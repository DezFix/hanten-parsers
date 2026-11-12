package hanten.wre.app.parsers.util

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NovelJsonContentParserTest {

	private val baseUrl = "https://ranobelib.me"

	@Test
	fun convertsParagraphsAndMarks() {
		val content = """
			{"type":"doc","content":[
				{"type":"paragraph","content":[
					{"type":"text","text":"Hello "},
					{"type":"text","text":"world","marks":[{"type":"bold"}]},
					{"type":"text","text":" & <tags>"}
				]},
				{"type":"blockquote","content":[{"type":"paragraph","content":[{"type":"text","text":"Quote"}]}]}
			]}
		""".trimIndent()

		val html = NovelJsonContentParser.toHtml(content, null, baseUrl)

		assertEquals(
			"<p>Hello <strong>world</strong> &amp; &lt;tags&gt;</p><blockquote><p>Quote</p></blockquote>",
			html,
		)
	}

	@Test
	fun convertsNestedJsonObject() {
		val root = JSONObject(
			"""{"type":"doc","content":[{"type":"paragraph","content":[{"type":"text","text":"Text"}]}]}""",
		)

		assertEquals("<p>Text</p>", NovelJsonContentParser.toHtml(root, null, baseUrl))
	}

	@Test
	fun resolvesImageAttachments() {
		val content = """
			{"type":"doc","content":[
				{"type":"image","attrs":{"description":"Cover","images":[{"image":"abc-123"}]}}
			]}
		""".trimIndent()
		val attachments = JSONObject(
			"""{"name":"abc-123","extension":"jpg","url":"/uploads/ranobe/1/chapters/2/abc-123.jpg"}""",
		)

		val html = NovelJsonContentParser.toHtml(content, attachments, baseUrl)

		assertTrue(html.contains("src=\"https://ranobelib.me/uploads/ranobe/1/chapters/2/abc-123.jpg\""))
		assertTrue(html.contains("<figcaption>Cover</figcaption>"))
	}

	@Test
	fun convertsSummaryToPlainText() {
		val summary = """
			{"type":"doc","content":[
				{"type":"paragraph","content":[{"type":"text","text":"First line"}]},
				{"type":"paragraph","content":[{"type":"text","text":"Second line"}]}
			]}
		""".trimIndent()

		assertEquals("First line Second line", NovelJsonContentParser.toPlainText(summary, baseUrl))
	}

	@Test
	fun keepsPlainText() {
		assertEquals("plain text", NovelJsonContentParser.toHtml("plain text", null, baseUrl))
	}
}
