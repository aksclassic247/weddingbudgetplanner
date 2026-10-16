package com.stitchbook.app.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.ui.Tab
import com.stitchbook.app.ui.components.ColorCover
import com.stitchbook.app.ui.components.CraftName
import com.stitchbook.app.ui.components.SectionTitle
import com.stitchbook.app.ui.components.StepProgress
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach
import com.stitchbook.app.ui.theme.Cream

@Composable
fun HomeScreen(
    vm: ProjectsViewModel,
    onTutorial: (String) -> Unit,
    onProject: (String) -> Unit,
    onTab: (String) -> Unit,
) {
    val projects by vm.projects.collectAsState()
    val latest = projects.firstOrNull()
    val latestPattern = latest?.let { vm.content.pattern(it.patternId) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Coral),
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        "Easy to follow crochet & knit",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Step by step tutorials, patterns, a stitch counter, and project tracking. Free, with no ads.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = { onTab(Tab.Learn.route) },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Coral),
                    ) { Text("Start learning") }
                }
            }
        }

        if (latest != null && latestPattern != null) {
            item {
                Card(
                    onClick = { onProject(latest.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("CONTINUE YOUR PROJECT", style = MaterialTheme.typography.labelSmall, color = InkSoft)
                        Text("${latestPattern.emoji} ${latestPattern.title}", style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = Ink)
                        StepProgress(latest.done.count { it }, latest.done.size)
                    }
                }
            }
        }

        item { SectionTitle("Quick start") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuickTile(Icons.AutoMirrored.Filled.MenuBook, "Learn stitches", Modifier.weight(1f)) { onTab(Tab.Learn.route) }
                QuickTile(Icons.Filled.GridView, "Browse patterns", Modifier.weight(1f)) { onTab(Tab.Patterns.route) }
                QuickTile(Icons.Filled.Bookmark, "My projects", Modifier.weight(1f)) { onTab(Tab.Projects.route) }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuickTile(Icons.Filled.Build, "Shaping calculator", Modifier.weight(1f)) { onTab(Tab.Tools.route) }
                Spacer(Modifier.weight(2f))
            }
        }

        item { SectionTitle("Beginner friendly tutorials") }
        items(vm.content.tutorials.filter { it.level == "Easy" || it.level == "Beginner" }.take(4)) { tutorial ->
            Card(onClick = { onTutorial(tutorial.id) }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    ColorCover(hexColor(tutorial.color), 52.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(tutorial.title, fontWeight = FontWeight.Bold, color = Ink)
                        Text("${tutorial.minutes} min · ${CraftName(tutorial.craft)}", style = MaterialTheme.typography.bodySmall, color = InkSoft)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = InkSoft)
                }
            }
        }
    }
}

@Composable
private fun QuickTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, contentDescription = null, tint = Coral, modifier = Modifier.size(26.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = Ink)
        }
    }
}
