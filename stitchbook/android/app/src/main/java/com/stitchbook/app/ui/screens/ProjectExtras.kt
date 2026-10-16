package com.stitchbook.app.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.data.Project
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.CoralDark
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach
import com.stitchbook.core.RepeatMath
import com.stitchbook.core.ReminderMath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/** The extra sections on a project: reminder, repeat counters, photos and pattern PDFs. */
@Composable
fun ProjectExtras(vm: ProjectsViewModel, project: Project) {
    ReminderSection(vm, project)
    RepeatSection(vm, project)
    PhotoSection(vm, project)
    PdfSection(vm, project)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderSection(vm: ProjectsViewModel, project: Project) {
    val context = LocalContext.current
    var showPicker by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    val enabled = project.reminderHour != null && project.reminderMinute != null

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) showPicker = true else permissionDenied = true
    }

    fun onToggle(on: Boolean) {
        if (!on) {
            vm.setReminder(project.id, null, null)
            return
        }
        val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED
        if (needsPermission) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) else showPicker = true
    }

    SectionCard(title = "Daily reminder") {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    if (enabled) "Every day at ${formatTime(project.reminderHour!!, project.reminderMinute!!)}" else "Off",
                    fontWeight = FontWeight.SemiBold,
                    color = Ink,
                )
                Text("Get a nudge to pick up this project.", style = MaterialTheme.typography.bodySmall, color = InkSoft)
            }
            Switch(checked = enabled, onCheckedChange = { onToggle(it) })
        }
        if (permissionDenied) {
            Text("Notifications are off for StitchBook. Turn them on in phone settings to get reminders.",
                style = MaterialTheme.typography.bodySmall, color = CoralDark)
        }
        if (enabled) {
            OutlinedButton(onClick = { showPicker = true }) { Text("Change time") }
        }
    }

    if (showPicker) {
        val state = rememberTimePickerState(
            initialHour = project.reminderHour ?: 19,
            initialMinute = project.reminderMinute ?: 0,
            is24Hour = false,
        )
        AlertDialog(
            onDismissRequest = { showPicker = false },
            title = { Text("Reminder time") },
            text = { TimePicker(state = state) },
            confirmButton = {
                Button(onClick = {
                    if (ReminderMath.isValidTime(state.hour, state.minute)) {
                        vm.setReminder(project.id, state.hour, state.minute)
                    }
                    showPicker = false
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun RepeatSection(vm: ProjectsViewModel, project: Project) {
    var showAdd by remember { mutableStateOf(false) }

    SectionCard(title = "Repeat counters") {
        Text(
            "Follow a row counter to count pattern repeats, for example every 8 rows.",
            style = MaterialTheme.typography.bodySmall,
            color = InkSoft,
        )
        project.repeats.forEach { repeat ->
            val source = project.counters.firstOrNull { it.id == repeat.sourceId }
            val rows = source?.count ?: 0
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "${repeat.name}: ${RepeatMath.completedRepeats(rows, repeat.rowsPerRepeat)} done",
                    fontWeight = FontWeight.Bold,
                    color = Ink,
                )
                val into = RepeatMath.rowsIntoCurrentRepeat(rows, repeat.rowsPerRepeat)
                Text(
                    "${source?.name ?: "Source counter removed"}: $into of ${repeat.rowsPerRepeat} rows into this repeat",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkSoft,
                )
                LinearProgressIndicator(
                    progress = { into.toFloat() / repeat.rowsPerRepeat },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = Coral,
                    trackColor = Peach,
                )
                TextButton(onClick = { vm.removeRepeat(project.id, repeat.id) }) {
                    Text("Remove", color = InkSoft)
                }
            }
        }
        if (project.counters.isEmpty()) {
            Text("Add a row counter above first.", style = MaterialTheme.typography.bodySmall, color = InkSoft)
        } else {
            OutlinedButton(onClick = { showAdd = true }) { Text("Add repeat counter") }
        }
    }

    if (showAdd) {
        AddRepeatDialog(
            counters = project.counters.map { it.id to it.name },
            onDismiss = { showAdd = false },
            onAdd = { name, sourceId, rows ->
                vm.addRepeat(project.id, name, sourceId, rows)
                showAdd = false
            },
        )
    }
}

@Composable
private fun AddRepeatDialog(
    counters: List<Pair<String, String>>,
    onDismiss: () -> Unit,
    onAdd: (String, String, Int) -> Unit,
) {
    var name by remember { mutableStateOf("Repeats") }
    var rows by remember { mutableStateOf("") }
    var sourceId by remember { mutableStateOf(counters.first().first) }
    val parsedRows = rows.toIntOrNull()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New repeat counter") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                Text("Follow which counter?", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    counters.forEach { (id, label) ->
                        FilterChip(selected = sourceId == id, onClick = { sourceId = id }, label = { Text(label) })
                    }
                }
                OutlinedTextField(
                    value = rows,
                    onValueChange = { rows = it.filter(Char::isDigit) },
                    label = { Text("Rows in one repeat") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank() && parsedRows != null && parsedRows > 0,
                onClick = { onAdd(name.trim(), sourceId, parsedRows!!) },
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun PhotoSection(vm: ProjectsViewModel, project: Project) {
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) vm.addAttachment(project.id, "photos", uri)
    }

    SectionCard(title = "Progress photos") {
        Text("Save photos of your work as you go. They stay on this device.", style = MaterialTheme.typography.bodySmall, color = InkSoft)
        if (project.photos.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(project.photos) { name ->
                    val file = vm.attachmentFile(project.id, "photos", name)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Thumbnail(file)
                        TextButton(onClick = { vm.removeAttachment(project.id, "photos", name) }) {
                            Text("Remove", color = InkSoft)
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = {
            pickPhoto.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) { Text("Add photo") }
    }
}

@Composable
private fun PdfSection(vm: ProjectsViewModel, project: Project) {
    var viewing by remember { mutableStateOf<String?>(null) }
    val pickPdf = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) vm.addAttachment(project.id, "pdfs", uri)
    }

    SectionCard(title = "Your pattern PDFs") {
        Text("Add a PDF pattern from your files. It opens inside the app.", style = MaterialTheme.typography.bodySmall, color = InkSoft)
        project.pdfs.forEachIndexed { index, name ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PDF ${index + 1}", fontWeight = FontWeight.SemiBold, color = Ink, modifier = Modifier.weight(1f))
                TextButton(onClick = { viewing = name }) { Text("View") }
                TextButton(onClick = { vm.removeAttachment(project.id, "pdfs", name) }) { Text("Remove", color = InkSoft) }
            }
        }
        OutlinedButton(onClick = { pickPdf.launch(arrayOf("application/pdf")) }) { Text("Add PDF") }
    }

    viewing?.let { name ->
        AlertDialog(
            onDismissRequest = { viewing = null },
            confirmButton = { TextButton(onClick = { viewing = null }) { Text("Close") } },
            text = { PdfPages(vm.attachmentFile(project.id, "pdfs", name)) },
        )
    }
}

@Composable
private fun PdfPages(file: File) {
    val pages by produceState<List<androidx.compose.ui.graphics.ImageBitmap>>(initialValue = emptyList(), file) {
        value = withContext(Dispatchers.IO) { renderPages(file) }
    }
    LazyColumn(modifier = Modifier.height(480.dp)) {
        items(pages) { page ->
            Image(
                bitmap = page,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

/** Draws the first pages of a PDF to bitmaps. Limited to 20 pages to keep memory use reasonable. */
private fun renderPages(file: File, maxPages: Int = 20): List<androidx.compose.ui.graphics.ImageBitmap> {
    if (!file.exists()) return emptyList()
    val out = mutableListOf<androidx.compose.ui.graphics.ImageBitmap>()
    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
        PdfRenderer(descriptor).use { renderer ->
            val count = minOf(renderer.pageCount, maxPages)
            for (index in 0 until count) {
                renderer.openPage(index).use { page ->
                    val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
                    bitmap.eraseColor(android.graphics.Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    out += bitmap.asImageBitmap()
                }
            }
        }
    }
    return out
}

@Composable
private fun Thumbnail(file: File) {
    val bitmap = remember(file.path) {
        if (!file.exists()) null else BitmapFactory.decodeFile(file.path, BitmapFactory.Options().apply { inSampleSize = 4 })
    }
    if (bitmap != null) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(110.dp), contentScale = ContentScale.Crop)
    } else {
        Spacer(Modifier.size(110.dp))
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
            content()
        }
    }
}

private fun formatTime(hour: Int, minute: Int): String {
    val h12 = if (hour % 12 == 0) 12 else hour % 12
    val suffix = if (hour < 12) "am" else "pm"
    return "%d:%02d %s".format(h12, minute, suffix)
}
