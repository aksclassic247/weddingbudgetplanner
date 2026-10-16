package com.stitchbook.app.data

import android.content.Context
import com.stitchbook.core.RowCounter
import org.json.JSONArray
import org.json.JSONObject

/** Saves projects as JSON in SharedPreferences. A bad save is ignored, so the app still opens. */
class ProjectStore(context: Context) {

    private val prefs = context.getSharedPreferences("stitchbook", Context.MODE_PRIVATE)

    fun load(): List<Project> {
        // Projects saved by the earlier version use the v1 key. The format is compatible, so read it too.
        val raw = prefs.getString(KEY, null) ?: prefs.getString(LEGACY_KEY, null) ?: return emptyList()
        return decodeAll(raw)
    }

    fun save(projects: List<Project>) {
        prefs.edit().putString(KEY, encodeAll(projects)).apply()
    }

    companion object {
        private const val KEY = "projects.v2"
        private const val LEGACY_KEY = "projects.v1"

        /** The same JSON is used for on-device storage and for backup files. */
        fun encodeAll(projects: List<Project>): String {
            val array = JSONArray()
            projects.forEach { array.put(it.toJson()) }
            return array.toString(2)
        }

        /** Returns an empty list when the text is not a valid backup. */
        fun decodeAll(text: String): List<Project> = try {
            val array = JSONArray(text)
            List(array.length()) { array.getJSONObject(it).toProject() }
        } catch (error: Exception) {
            emptyList()
        }

        private fun Project.toJson(): JSONObject = JSONObject()
            .put("id", id)
            .put("patternId", patternId)
            .put("startedAt", startedAt)
            .put("done", JSONArray(done))
            .put("notes", notes)
            .put("counters", JSONArray().also { array ->
                counters.forEach { counter ->
                    array.put(
                        JSONObject()
                            .put("id", counter.id)
                            .put("name", counter.name)
                            .put("count", counter.count)
                            .put("target", counter.target ?: JSONObject.NULL)
                            .put("previousCount", counter.previousCount ?: JSONObject.NULL)
                    )
                }
            })
            .put("repeats", JSONArray().also { array ->
                repeats.forEach { repeat ->
                    array.put(
                        JSONObject()
                            .put("id", repeat.id)
                            .put("name", repeat.name)
                            .put("sourceId", repeat.sourceId)
                            .put("rowsPerRepeat", repeat.rowsPerRepeat)
                    )
                }
            })
            .put("reminderHour", reminderHour ?: JSONObject.NULL)
            .put("reminderMinute", reminderMinute ?: JSONObject.NULL)
            .put("pdfs", JSONArray(pdfs))
            .put("photos", JSONArray(photos))

        private fun JSONObject.toProject(): Project {
            val doneArray = getJSONArray("done")
            val counterArray = optJSONArray("counters") ?: JSONArray()
            val repeatArray = optJSONArray("repeats") ?: JSONArray()
            val pdfArray = optJSONArray("pdfs") ?: JSONArray()
            val photoArray = optJSONArray("photos") ?: JSONArray()
            return Project(
                id = getString("id"),
                patternId = getString("patternId"),
                startedAt = getLong("startedAt"),
                done = List(doneArray.length()) { doneArray.getBoolean(it) },
                notes = optString("notes", ""),
                counters = List(counterArray.length()) { index ->
                    val c = counterArray.getJSONObject(index)
                    RowCounter(
                        name = c.getString("name"),
                        count = c.getInt("count"),
                        target = if (c.isNull("target")) null else c.getInt("target"),
                        id = c.optString("id").ifBlank { java.util.UUID.randomUUID().toString() },
                        previousCount = if (c.isNull("previousCount")) null else c.getInt("previousCount"),
                    )
                },
                repeats = List(repeatArray.length()) { index ->
                    val r = repeatArray.getJSONObject(index)
                    RepeatCounter(
                        id = r.getString("id"),
                        name = r.getString("name"),
                        sourceId = r.getString("sourceId"),
                        rowsPerRepeat = r.getInt("rowsPerRepeat"),
                    )
                },
                reminderHour = if (isNull("reminderHour")) null else getInt("reminderHour"),
                reminderMinute = if (isNull("reminderMinute")) null else getInt("reminderMinute"),
                pdfs = List(pdfArray.length()) { pdfArray.getString(it) },
                photos = List(photoArray.length()) { photoArray.getString(it) },
            )
        }
    }
}
