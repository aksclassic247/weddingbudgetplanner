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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stitchbook.app.data.Tutorial
import com.stitchbook.app.ui.components.ColorCover
import com.stitchbook.app.ui.components.CraftName
import com.stitchbook.app.ui.components.LevelBadge
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.CoralDark
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(tutorial: Tutorial?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tutorial?.title ?: "Tutorial") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                },
            )
        },
    ) { padding ->
        if (tutorial == null) {
            Text("This lesson could not be found.", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
        ) {
            item {
                Box(
                    Modifier.fillMaxWidth().height(130.dp).background(hexColor(tutorial.color), androidx.compose.foundation.shape.RoundedCornerShape(24.dp)).padding(18.dp),
                    contentAlignment = Alignment.BottomStart,
                ) {
                    Column {
                        Text(CraftName(tutorial.craft).uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.White)
                        Text(tutorial.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LevelBadge(tutorial.level)
                    Text("${tutorial.minutes} min", style = MaterialTheme.typography.labelMedium, color = InkSoft)
                }
            }
            item { Text(tutorial.summary, style = MaterialTheme.typography.bodyLarge, color = Ink) }
            item { Text("Step by step", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink) }
            itemsIndexed(tutorial.steps) { index, step ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            Modifier.size(30.dp).background(Peach, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("${index + 1}", fontWeight = FontWeight.ExtraBold, color = CoralDark)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(step.title, fontWeight = FontWeight.Bold, color = Ink)
                            Text(step.text, style = MaterialTheme.typography.bodyMedium, color = InkSoft)
                        }
                    }
                }
            }
        }
    }
}
