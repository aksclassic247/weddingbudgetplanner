package com.stitchbook.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items as rowItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.ui.components.CraftName
import com.stitchbook.app.ui.components.LevelBadge
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft

private val levels = listOf("All", "Easy", "Beginner", "Intermediate", "Advanced")

@Composable
fun PatternsScreen(vm: ProjectsViewModel, onPattern: (String) -> Unit) {
    var level by remember { mutableStateOf("All") }
    var query by remember { mutableStateOf("") }
    val visible = vm.content.patterns.filter { pattern ->
        (level == "All" || pattern.level == level) &&
            (query.isBlank() ||
                pattern.title.contains(query.trim(), ignoreCase = true) ||
                CraftName(pattern.craft).contains(query.trim(), ignoreCase = true))
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Get inspired", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Ink)
        Text("Patterns for every skill level. Start one and track each round.", style = MaterialTheme.typography.bodyMedium, color = InkSoft)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Search patterns") },
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rowItems(levels) { label ->
                FilterChip(selected = level == label, onClick = { level = label }, label = { Text(label) })
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Adaptive(minSize = 160.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            items(visible) { pattern ->
                Card(
                    onClick = { onPattern(pattern.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Column {
                        Box(
                            Modifier.fillMaxWidth().height(110.dp).background(hexColor(pattern.color)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(pattern.emoji, style = MaterialTheme.typography.displaySmall)
                        }
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(pattern.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Ink)
                            LevelBadge(pattern.level)
                            Text(CraftName(pattern.craft), style = MaterialTheme.typography.labelMedium, color = InkSoft)
                        }
                    }
                }
            }
        }
    }
}
