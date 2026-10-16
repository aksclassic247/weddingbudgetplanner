package com.stitchbook.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ToolsScreen() {
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

    Column(
        Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Shaping calculator", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Ink)
        Text(
            "Spread increases or decreases evenly across a row. Plain arithmetic, so the answer is the same every time.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkSoft,
        )

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
            outcome == null -> Text("Enter the stitch count and how many to make.", style = MaterialTheme.typography.bodyMedium, color = InkSoft)
            outcome.isSuccess -> Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Work it like this", style = MaterialTheme.typography.labelLarge, color = CoralDark, fontWeight = FontWeight.Bold)
                    Text(outcome.getOrThrow(), style = MaterialTheme.typography.bodyLarge, color = Ink)
                }
            }
            else -> Text(outcome.exceptionOrNull()?.message ?: "Those numbers do not work.", color = CoralDark)
        }
    }
}
