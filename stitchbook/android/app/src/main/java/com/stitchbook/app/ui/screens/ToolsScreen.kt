package com.stitchbook.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.stitchbook.app.ui.theme.CoralDark
import com.stitchbook.app.ui.theme.Ink
import com.stitchbook.app.ui.theme.InkSoft
import com.stitchbook.core.ShapingCalculator
import com.stitchbook.core.ShapingType
import com.stitchbook.core.YarnCalculator

@Composable
fun ToolsScreen() {
    Column(
        Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Tools", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Ink)
        ShapingSection()
        YarnSection()
    }
}

@Composable
private fun ShapingSection() {
    var type by remember { mutableStateOf(ShapingType.INCREASE) }
    var stitches by remember { mutableStateOf("") }
    var changes by remember { mutableStateOf("") }

    val outcome: Result<String>? = if (stitches.isBlank() || changes.isBlank()) {
        null
    } else {
        runCatching {
            val plan = ShapingCalculator.plan(stitches.toInt(), changes.toInt(), type)
            "${plan.describe()}\n\nYou will have ${plan.stitchesAfter} stitches after this row."
        }
    }

    ToolCard(title = "Shaping calculator", subtitle = "Spread increases or decreases evenly across a row.") {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            ShapingType.entries.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = type == option,
                    onClick = { type = option },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = ShapingType.entries.size),
                ) { Text(option.label) }
            }
        }
        OutlinedTextField(
            value = stitches,
            onValueChange = { stitches = it.filter(Char::isDigit) },
            label = { Text("Stitches in the row now") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = changes,
            onValueChange = { changes = it.filter(Char::isDigit) },
            label = { Text("How many ${type.label.lowercase()}s") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        when {
            outcome == null -> Text("Enter the stitch count and how many to make.", color = InkSoft)
            outcome.isSuccess -> Text("Work it like this", color = CoralDark, fontWeight = FontWeight.Bold)
            else -> Text(outcome.exceptionOrNull()?.message ?: "Those numbers do not work.", color = CoralDark)
        }
        outcome?.getOrNull()?.let { text ->
            Text(text, style = MaterialTheme.typography.bodyLarge, color = Ink)
        }
    }
}

@Composable
private fun YarnSection() {
    var projectGrams by remember { mutableStateOf("") }
    var ballGrams by remember { mutableStateOf("") }
    var ballMetres by remember { mutableStateOf("") }

    val outcome: Result<String>? = if (projectGrams.isBlank() || ballGrams.isBlank() || ballMetres.isBlank()) {
        null
    } else {
        runCatching {
            val estimate = YarnCalculator.estimate(
                projectGrams = projectGrams.toDouble(),
                ballGrams = ballGrams.toDouble(),
                ballMetres = ballMetres.toDouble(),
            )
            "You need about ${estimate.metres.toInt()} m of yarn, which is ${estimate.balls} ball(s). " +
                "This includes a 10 percent margin."
        }
    }

    ToolCard(title = "Yarn estimate", subtitle = "Read the ball weight and length from the yarn label.") {
        OutlinedTextField(
            value = projectGrams,
            onValueChange = { projectGrams = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Grams the project needs") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = ballGrams,
            onValueChange = { ballGrams = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Grams per ball, from the label") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = ballMetres,
            onValueChange = { ballMetres = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Metres per ball, from the label") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )
        when (outcome) {
            null -> Text("Fill in all three numbers.", color = InkSoft)
            else -> outcome.fold(
                onSuccess = { Text(it, style = MaterialTheme.typography.bodyLarge, color = Ink) },
                onFailure = { Text(it.message ?: "Those numbers do not work.", color = CoralDark) },
            )
        }
    }
}

@Composable
private fun ToolCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = InkSoft)
            content()
        }
    }
}
