package com.stitchbook.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.ui.components.ColorCover
import com.stitchbook.app.ui.components.CraftName
import com.stitchbook.app.ui.components.LevelBadge
import com.stitchbook.app.ui.components.hexColor
import com.stitchbook.app.ui.theme.CoralDark
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft

@Composable
fun LearnScreen(vm: ProjectsViewModel, onTutorial: (String) -> Unit) {
    var craft by remember { mutableStateOf("knitting") }
    val tutorials = vm.content.tutorials.filter { it.craft == craft }
    val stitches = vm.content.stitches.filter { it.craft == craft }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
    ) {
        item {
            Text("Learn", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Ink)
        }
        item {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                listOf("knitting" to "Knitting", "crochet" to "Crochet").forEachIndexed { index, (value, label) ->
                    SegmentedButton(
                        selected = craft == value,
                        onClick = { craft = value },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                    ) { Text(label) }
                }
            }
        }
        item { Text("Tutorials", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink) }
        items(tutorials) { tutorial ->
            Card(onClick = { onTutorial(tutorial.id) }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    ColorCover(hexColor(tutorial.color), 64.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(tutorial.title, fontWeight = FontWeight.Bold, color = Ink)
                        Text(tutorial.summary, style = MaterialTheme.typography.bodySmall, color = InkSoft)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            LevelBadge(tutorial.level)
                            Text("${tutorial.minutes} min", style = MaterialTheme.typography.labelMedium, color = InkSoft)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = InkSoft)
                }
            }
        }
        item { Text("Abbreviations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink) }
        items(stitches) { stitch ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stitch.abbr, modifier = Modifier.width(64.dp), fontWeight = FontWeight.Bold, color = CoralDark)
                    Column {
                        Text(stitch.name, fontWeight = FontWeight.SemiBold, color = Ink)
                        Text(stitch.meaning, style = MaterialTheme.typography.bodySmall, color = InkSoft)
                    }
                }
            }
        }
    }
}
