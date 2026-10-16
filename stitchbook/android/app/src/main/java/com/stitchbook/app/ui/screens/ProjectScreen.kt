package com.stitchbook.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.data.Pattern
import com.stitchbook.app.data.Project
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.CoralDark
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach
import com.stitchbook.app.ui.theme.Success
import com.stitchbook.core.RowCounter
import com.stitchbook.core.StitchCheck
import com.stitchbook.core.StitchCheckResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(vm: ProjectsViewModel, projectId: String, onBack: () -> Unit) {
    val projects by vm.projects.collectAsState()
    val project = projects.firstOrNull { it.id == projectId }
    val pattern = project?.let { vm.content.pattern(it.patternId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pattern?.title ?: "Project") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                },
            )
        },
    ) { padding ->
        if (project == null || pattern == null) {
            Text("This project was removed.", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        ProjectBody(vm, project, pattern, Modifier.padding(padding), onRemoved = onBack)
    }
}

@Composable
private fun ProjectBody(vm: ProjectsViewModel, project: Project, pattern: Pattern, modifier: Modifier, onRemoved: () -> Unit) {
    val haptics = LocalHapticFeedback.current
    var checkingStep by remember { mutableStateOf<Int?>(null) }
    var showAddCounter by remember { mutableStateOf(false) }
    val done = project.done.count { it }

    Column(
        modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            Modifier.fillMaxWidth().height(120.dp).background(com.stitchbook.app.ui.components.hexColor(pattern.color), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(pattern.emoji, style = MaterialTheme.typography.displayMedium)
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = { if (project.done.isEmpty()) 0f else done.toFloat() / project.done.size },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Coral,
                    trackColor = Peach,
                )
                Text("$done/${project.done.size} steps done", style = MaterialTheme.typography.labelMedium, color = InkSoft)
                if (project.done.isNotEmpty() && done == project.done.size) {
                    Text("All steps done. Nice work!", style = MaterialTheme.typography.labelLarge, color = Success)
                }
            }
        }

        Text("Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
        pattern.steps.forEachIndexed { index, step ->
            val checked = project.done.getOrElse(index) { false }
            Card(colors = CardDefaults.cardColors(containerColor = if (checked) Peach else Color.White)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { vm.toggleStep(project.id, index) },
                            colors = CheckboxDefaults.colors(checkedColor = Coral),
                        )
                        Spacer(Modifier.width(4.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                step.label,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = CoralDark,
                            )
                            Text(
                                step.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (checked) InkSoft else Ink,
                                textDecoration = if (checked) TextDecoration.LineThrough else null,
                            )
                        }
                    }
                    step.stitches?.let { expected ->
                        OutlinedButton(onClick = { checkingStep = index }) {
                            Text("Check my count (pattern says $expected)")
                        }
                    }
                }
            }
        }

        Text("Row counters", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
        if (project.counters.isEmpty()) {
            Text("Add a counter to keep track of rows, repeats or pattern sections.", style = MaterialTheme.typography.bodySmall, color = InkSoft)
        }
        project.counters.forEachIndexed { index, counter ->
            CounterCard(
                counter = counter,
                onChange = { updated ->
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    vm.update(project.id) { p ->
                        p.copy(counters = p.counters.toMutableList().also { it[index] = updated })
                    }
                },
                onRemove = {
                    vm.update(project.id) { p -> p.copy(counters = p.counters.filterIndexed { i, _ -> i != index }) }
                },
            )
        }
        OutlinedButton(onClick = { showAddCounter = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Add counter")
        }

        ProjectExtras(vm, project)

        Text("Notes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
        OutlinedTextField(
            value = project.notes,
            onValueChange = { text -> vm.update(project.id) { it.copy(notes = text) } },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            placeholder = { Text("Yarn colors, hook size changes, anything to remember") },
        )

        TextButton(onClick = { vm.remove(project.id); onRemoved() }, modifier = Modifier.fillMaxWidth()) {
            Text("Remove project", color = CoralDark)
        }
    }

    checkingStep?.let { index ->
        val expected = pattern.steps[index].stitches ?: return@let
        StitchCheckDialog(
            expected = expected,
            onDismiss = { checkingStep = null },
        )
    }

    if (showAddCounter) {
        AddCounterDialog(
            onDismiss = { showAddCounter = false },
            onAdd = { counter ->
                vm.update(project.id) { p -> p.copy(counters = p.counters + counter) }
                showAddCounter = false
            },
        )
    }
}

@Composable
private fun CounterCard(counter: RowCounter, onChange: (RowCounter) -> Unit, onRemove: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(counter.name, fontWeight = FontWeight.Bold, color = Ink)
                    Text(
                        if (counter.target != null) "${counter.count} of ${counter.target}" else "${counter.count}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Coral,
                    )
                }
                IconButton(onClick = { onChange(counter.decrement()) }) { Icon(Icons.Filled.Remove, contentDescription = "Minus one") }
                IconButton(onClick = { onChange(counter.increment()) }) { Icon(Icons.Filled.Add, contentDescription = "Plus one") }
                IconButton(onClick = { onChange(counter.reset()) }) { Icon(Icons.Filled.Refresh, contentDescription = "Reset") }
            }
            counter.progress?.let { fraction ->
                LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth().height(6.dp), color = Coral, trackColor = Peach)
                if (counter.isComplete) {
                    Text("Target reached", style = MaterialTheme.typography.labelMedium, color = Success)
                }
            }
            TextButton(onClick = onRemove) { Text("Remove counter", color = InkSoft) }
        }
    }
}

@Composable
private fun AddCounterDialog(onDismiss: () -> Unit, onAdd: (RowCounter) -> Unit) {
    var name by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    val parsedTarget = target.toIntOrNull()
    val valid = name.isNotBlank() && (target.isBlank() || (parsedTarget != null && parsedTarget > 0))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New counter") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name, for example Rows") }, singleLine = true)
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it.filter(Char::isDigit) },
                    label = { Text("Target (optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = { onAdd(RowCounter(name.trim(), target = parsedTarget)) },
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun StitchCheckDialog(expected: Int, onDismiss: () -> Unit) {
    var entry by remember { mutableStateOf("") }
    val counted = entry.toIntOrNull()
    val message = counted?.let { StitchCheck.message(StitchCheck.compare(expected, it), expected) }
    val isMatch = counted != null && StitchCheck.compare(expected, counted) == StitchCheckResult.Match

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Check your stitch count") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Count the stitches in this round or row, then enter the number.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = entry,
                    onValueChange = { entry = it.filter(Char::isDigit) },
                    label = { Text("Your count") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                message?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold,
                        color = if (isMatch) Success else CoralDark)
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Done") } },
    )
}
