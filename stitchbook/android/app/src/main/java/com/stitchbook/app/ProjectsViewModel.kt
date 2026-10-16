package com.stitchbook.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.stitchbook.app.data.Content
import com.stitchbook.app.data.ContentRepository
import com.stitchbook.app.data.Project
import com.stitchbook.app.data.ProjectStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val store = ProjectStore(application)
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

    fun remove(projectId: String) {
        commit(_projects.value.filterNot { it.id == projectId })
    }

    private fun commit(list: List<Project>) {
        _projects.value = list
        store.save(list)
    }
}
