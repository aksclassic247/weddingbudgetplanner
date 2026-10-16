package com.stitchbook.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Reads assets/content.json, which is shared with the iOS app. */
object ContentRepository {

    fun load(context: Context): Content {
        val text = context.assets.open("content.json").bufferedReader().use { it.readText() }
        val root = JSONObject(text)
        return Content(
            tutorials = root.getJSONArray("tutorials").map { it.toTutorial() },
            stitches = root.getJSONArray("stitches").map { it.toStitch() },
            patterns = root.getJSONArray("patterns").map { it.toPattern() },
        )
    }

    private fun JSONObject.toTutorial() = Tutorial(
        id = getString("id"),
        craft = getString("craft"),
        title = getString("title"),
        level = getString("level"),
        minutes = getInt("minutes"),
        color = getString("color"),
        summary = getString("summary"),
        steps = getJSONArray("steps").map {
            TutorialStep(title = it.getString("title"), text = it.getString("text"))
        },
    )

    private fun JSONObject.toStitch() = Stitch(
        abbr = getString("abbr"),
        name = getString("name"),
        craft = getString("craft"),
        meaning = getString("meaning"),
    )

    private fun JSONObject.toPattern() = Pattern(
        id = getString("id"),
        craft = getString("craft"),
        title = getString("title"),
        level = getString("level"),
        color = getString("color"),
        emoji = getString("emoji"),
        time = getString("time"),
        materials = getJSONArray("materials").strings(),
        gauge = optString("gauge", "").ifBlank { null },
        credit = optString("credit", ""),
        steps = getJSONArray("steps").map {
            PatternStep(
                label = it.getString("label"),
                text = it.getString("text"),
                stitches = if (it.isNull("stitches")) null else it.getInt("stitches"),
            )
        },
    )

    private inline fun <T> JSONArray.map(transform: (JSONObject) -> T): List<T> =
        List(length()) { index -> transform(getJSONObject(index)) }

    private fun JSONArray.strings(): List<String> = List(length()) { index -> getString(index) }
}
