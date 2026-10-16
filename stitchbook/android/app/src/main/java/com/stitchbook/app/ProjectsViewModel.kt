package com.stitchbook.app

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.stitchbook.app.data.Content
import com.stitchbook.app.data.ContentRepository
import com.stitchbook.app.data.Project
import com.stitchbook.app.data.ProjectFiles
import com.stitchbook.app.data.ProjectStore
import com.stitchbook.app.data.RepeatCounter
import com.stitchbook.app.reminders.ReminderScheduler
import com.stitchbook.core.RowCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val store = ProjectStore(application)
    private val files = ProjectFiles(application)
    val content: Content = ContentRepository.load(application)

    private val _projects = MutableStateFlow(store.load())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    /** Starts a project from a pattern and returns its id, or null if the pattern is unknown. */
    fun start(patternId: String): String? {
        val pattern = content.pattern(patternId) ?: return null
        val project = Project(
            id = "$patternId-${System.currentTimeMillis()}",
            patternId = patternId,
            startedAt = System.currentTimeMillis(),
            done = List(pattern.steps.size) { false },
        )
        commit(listOf(project) + _projects.value)
        return project.id
    }

    fun update(projectId: String, change: (Project) -> Project) {
        commit(_projects.value.map { if (it.id == projectId) change(it) else it })
    }

    fun toggleStep(projectId: String, index: Int) = update(projectId) { project ->
        val done = project.done.toMutableList()
        done[index] = !done[index]
        project.copy(done = done)
    }

    fun addRepeat(projectId: String, name: String, sourceId: String, rowsPerRepeat: Int) {
        update(projectId) { project ->
            project.copy(repeats = project.repeats + RepeatCounter(UUID.randomUUID().toString(), name, sourceId, rowsPerRepeat))
        }
    }

    fun removeRepeat(projectId: String, repeatId: String) {
        update(projectId) { project -> project.copy(repeats = project.repeats.filterNot { it.id == repeatId }) }
    }

    /** Turns the daily reminder on at [hour]:[minute], or off when both are null. */
    fun setReminder(projectId: String, hour: Int?, minute: Int?) {
        val project = _projects.value.firstOrNull { it.id == projectId } ?: return
        update(projectId) { it.copy(reminderHour = hour, reminderMinute = minute) }
        if (hour != null && minute != null) {
            val title = content.pattern(project.patternId)?.title.orEmpty()
            ReminderScheduler.schedule(getApplication(), projectId, title, hour, minute)
        } else {
            ReminderScheduler.cancel(getApplication(), projectId)
        }
    }

    /** Copies a PDF ("pdfs") or a photo ("photos") into the project. */
    fun addAttachment(projectId: String, kind: String, uri: Uri) {
        val name = files.copyIn(uri, projectId, kind)
        update(projectId) { project ->
            if (kind == "pdfs") project.copy(pdfs = project.pdfs + name) else project.copy(photos = project.photos + name)
        }
    }

    fun removeAttachment(projectId: String, kind: String, name: String) {
        files.delete(projectId, kind, name)
        update(projectId) { project ->
            if (kind == "pdfs") project.copy(pdfs = project.pdfs - name) else project.copy(photos = project.photos - name)
        }
    }

    fun attachmentFile(projectId: String, kind: String, name: String) = files.file(projectId, kind, name)

    fun remove(projectId: String) {
        ReminderScheduler.cancel(getApplication(), projectId)
        files.deleteProject(projectId)
        commit(_projects.value.filterNot { it.id == projectId })
    }

    /** The JSON text for a backup file. Photos and PDFs are not included. */
    fun backupText(): String = ProjectStore.encodeAll(_projects.value)

    /** Adds projects from a backup. Projects with an id that already exists are skipped. Returns how many were added. */
    fun restore(text: String): Int {
        val incoming = ProjectStore.decodeAll(text)
        val existing = _projects.value.map { it.id }.toSet()
        val added = incoming.filterNot { it.id in existing }
        if (added.isNotEmpty()) commit(added + _projects.value)
        return added.size
    }

    /** The counter's name and id, used by the repeat counter picker. */
    fun counterOptions(projectId: String): List<RowCounter> =
        _projects.value.firstOrNull { it.id == projectId }?.counters.orEmpty()

    private fun commit(list: List<Project>) {
        _projects.value = list
        store.save(list)
    }
}
