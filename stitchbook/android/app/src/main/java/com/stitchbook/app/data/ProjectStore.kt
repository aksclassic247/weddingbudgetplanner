package com.stitchbook.app.data

import android.content.Context
import com.stitchbook.core.RowCounter
import org.json.JSONArray
import org.json.JSONObject

/** Saves projects as JSON in SharedPreferences. A bad save is ignored, so the app still opens. */
class ProjectStore(context: Context) {

    private val prefs = context.getSharedPreferences("stitchbook", Context.MODE_PRIVATE)

    fun load(): List<Project> {
        val raw = prefs.getString(KEY, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            List(array.length()) { array.getJSONObject(it).toProject() }
        } catch (error: Exception) {
            emptyList()
        }
    }

    fun save(projects: List<Project>) {
        val array = JSONArray()
        projects.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY, array.toString()).apply()
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
                        .put("name", counter.name)
                        .put("count", counter.count)
                        .put("target", counter.target ?: JSONObject.NULL)
                )
            }
        })

    private fun JSONObject.toProject(): Project {
        val doneArray = getJSONArray("done")
        val counterArray = optJSONArray("counters") ?: JSONArray()
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
                )
            },
        )
    }

    private companion object {
        const val KEY = "projects.v1"
    }
}
