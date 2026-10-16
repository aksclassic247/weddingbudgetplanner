package com.stitchbook.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Coral = Color(0xFFE76F5E)
val CoralDark = Color(0xFFC9523F)
val Peach = Color(0xFFFBE8DA)
val PeachDeep = Color(0xFFF6D5C0)
val Cream = Color(0xFFFFF8F3)
val Ink = Color(0xFF2B2320)
val InkSoft = Color(0xFF6E615B)
val LineColor = Color(0xFFEEDFD6)
val Success = Color(0xFF5E9E63)

private val scheme = lightColorScheme(
    primary = Coral,
    onPrimary = Color.White,
    primaryContainer = Peach,
    onPrimaryContainer = Ink,
    secondary = CoralDark,
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Peach,
    onSurfaceVariant = InkSoft,
    outline = LineColor,
)

@Composable
fun StitchBookTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, content = content)
}
