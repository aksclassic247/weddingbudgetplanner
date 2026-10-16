package com.stitchbook.app.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * Copies pattern PDFs and progress photos into the app's private storage.
 * Files stay on the device and are removed when their project is deleted.
 */
class ProjectFiles(private val context: Context) {

    fun folder(projectId: String, kind: String): File =
        File(context.filesDir, "projects/$projectId/$kind").apply { mkdirs() }

    fun file(projectId: String, kind: String, name: String): File = File(folder(projectId, kind), name)

    /** Copies [uri] into the project's folder and returns the new file name. */
    fun copyIn(uri: Uri, projectId: String, kind: String): String {
        val mime = context.contentResolver.getType(uri).orEmpty()
        val extension = when {
            mime == "application/pdf" -> "pdf"
            mime == "image/png" -> "png"
            else -> "jpg"
        }
        val name = "${UUID.randomUUID()}.$extension"
        context.contentResolver.openInputStream(uri)?.use { input ->
            file(projectId, kind, name).outputStream().use { output -> input.copyTo(output) }
        } ?: throw IllegalStateException("The selected file could not be opened.")
        return name
    }

    fun delete(projectId: String, kind: String, name: String) {
        file(projectId, kind, name).delete()
    }

    fun deleteProject(projectId: String) {
        File(context.filesDir, "projects/$projectId").deleteRecursively()
    }
}
