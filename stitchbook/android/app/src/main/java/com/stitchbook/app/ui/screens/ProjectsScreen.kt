package com.stitchbook.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.data.Project
import com.stitchbook.app.ui.components.ColorCover
import com.stitchbook.app.ui.components.StepProgress
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Success

@Composable
fun ProjectsScreen(vm: ProjectsViewModel, onProject: (String) -> Unit) {
    val projects by vm.projects.collectAsState()
    val context = LocalContext.current
    var message by remember { mutableStateOf<String?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            val written = runCatching {
                context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(vm.backupText()) }
            }.isSuccess
            message = if (written) "Backup saved." else "The backup could not be saved."
        }
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val text = runCatching {
                context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
            }.getOrDefault("")
            val added = vm.restore(text)
            message = if (added > 0) "Added $added project(s) from the backup." else "No new projects were found in that file."
        }
    }

    val header: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { exportLauncher.launch("stitchbook-backup.json") }) { Text("Export backup") }
                OutlinedButton(onClick = { importLauncher.launch(arrayOf("application/json", "text/plain")) }) { Text("Restore backup") }
            }
            message?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = InkSoft) }
        }
    }

    if (projects.isEmpty()) {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            header()
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("No projects yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
                    Text(
                        "Pick a pattern and start a project. Your progress is saved on this device.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkSoft,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
    ) {
        item {
            Text("My projects", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Ink)
        }
        item { header() }
        items(projects) { project -> ProjectRow(vm, project, onClick = { onProject(project.id) }) }
    }
}

@Composable
private fun ProjectRow(vm: ProjectsViewModel, project: Project, onClick: () -> Unit) {
    val pattern = vm.content.pattern(project.patternId) ?: return
    val done = project.done.count { it }
    Card(onClick = onClick, colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ColorCover(hexColor(pattern.color), 64.dp) {
                Text(pattern.emoji, style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(pattern.title, fontWeight = FontWeight.Bold, color = Ink)
                StepProgress(done, project.done.size)
                if (done == project.done.size && done > 0) {
                    Text("Finished", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Success)
                }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = InkSoft)
        }
    }
}
