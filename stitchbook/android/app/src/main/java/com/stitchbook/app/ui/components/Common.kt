package com.stitchbook.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ui.theme.Coral
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.app.ui.theme.Peach

/** Turns a "#RRGGBB" string from the content file into a Compose color. */
fun hexColor(hex: String): Color = Color(android.graphics.Color.parseColor(hex))

@Composable
fun LevelBadge(level: String) {
    val tint = when (level) {
        "Easy" -> Color(0xFF7BC47F)
        "Beginner" -> Color(0xFFF2A65A)
        "Intermediate" -> Coral
        else -> Color(0xFF8E6CC9)
    }
    Row(
        modifier = Modifier
            .background(tint.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).background(tint, RoundedCornerShape(50)))
        Spacer(Modifier.width(6.dp))
        Text(level, style = MaterialTheme.typography.labelMedium, color = Ink, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StepProgress(done: Int, total: Int) {
    val fraction = if (total == 0) 0f else done.toFloat() / total
    Column {
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = Coral,
            trackColor = Peach,
        )
        Spacer(Modifier.height(4.dp))
        Text("$done/$total done", style = MaterialTheme.typography.labelMedium, color = InkSoft)
    }
}

@Composable
fun CraftName(craft: String): String = if (craft == "knitting") "Knitting" else "Crochet"

/** A colored block used in place of photos, so the app does not need any image assets. */
@Composable
fun ColorCover(color: Color, size: androidx.compose.ui.unit.Dp, content: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier.size(size).background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) { content() }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink,
        modifier = Modifier.padding(top = 8.dp))
}
