package com.stitchbook.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.data.Pattern
import com.stitchbook.app.ui.components.CraftName
import com.stitchbook.app.ui.components.LevelBadge
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternDetailScreen(
    vm: ProjectsViewModel,
    pattern: Pattern?,
    onBack: () -> Unit,
    onStarted: (String) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pattern?.title ?: "Pattern") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                },
            )
        },
    ) { padding ->
        if (pattern == null) {
            Text("This pattern could not be found.", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        Column(
            Modifier.fillMaxWidth().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                Modifier.fillMaxWidth().height(170.dp).background(hexColor(pattern.color), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(pattern.emoji, style = MaterialTheme.typography.displayLarge)
            }
            Text(pattern.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Ink)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LevelBadge(pattern.level)
                Text(CraftName(pattern.craft), style = MaterialTheme.typography.labelMedium, color = InkSoft)
                Text(pattern.time, style = MaterialTheme.typography.labelMedium, color = InkSoft)
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Materials", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Ink)
                    pattern.materials.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium, color = Ink) }
                    pattern.gauge?.let { Text("Gauge: $it", style = MaterialTheme.typography.bodySmall, color = InkSoft) }
                }
            }

            Text("Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
            pattern.steps.forEach { step ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(step.label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Coral)
                        Text(step.text, style = MaterialTheme.typography.bodyMedium, color = Ink)
                        step.stitches?.let {
                            Text("$it stitches", style = MaterialTheme.typography.labelSmall, color = InkSoft)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    vm.start(pattern.id)?.let(onStarted)
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Start this project") }

            TextButton(onClick = {
                com.stitchbook.app.support.Support.openMail(context, "Pattern problem: ${pattern.title}")
            }) { Text("Report a problem with this pattern", color = CoralDark) }
            Card(colors = CardDefaults.cardColors(containerColor = if (pattern.testedOn.isNullOrBlank()) Peach else Color.White)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(pattern.testLabel, fontWeight = FontWeight.Bold, color = Ink)
                    if (pattern.credit.isNotBlank()) {
                        Text(pattern.credit, style = MaterialTheme.typography.bodySmall, color = InkSoft)
                    }
                }
            }
            Text(
                "Check these instructions against your own yarn and tools before you begin.",
                style = MaterialTheme.typography.bodySmall,
                color = InkSoft,
            )
        }
    }
}
