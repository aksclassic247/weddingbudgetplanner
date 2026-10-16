package com.stitchbook.app.data

import com.stitchbook.core.RowCounter

data class TutorialStep(val title: String, val text: String)

data class Tutorial(
    val id: String,
    val craft: String,
    val title: String,
    val level: String,
    val minutes: Int,
    val color: String,
    val summary: String,
    val steps: List<TutorialStep>,
)

data class Stitch(val abbr: String, val name: String, val craft: String, val meaning: String)

data class PatternStep(val label: String, val text: String, val stitches: Int?)

data class Pattern(
    val id: String,
    val craft: String,
    val title: String,
    val level: String,
    val color: String,
    val emoji: String,
    val time: String,
    val materials: List<String>,
    val gauge: String?,
    val steps: List<PatternStep>,
)

data class Content(
    val tutorials: List<Tutorial>,
    val stitches: List<Stitch>,
    val patterns: List<Pattern>,
) {
    fun tutorial(id: String) = tutorials.firstOrNull { it.id == id }
    fun pattern(id: String) = patterns.firstOrNull { it.id == id }
}

/** One project a person started from a pattern. Saved on the device. */
data class Project(
    val id: String,
    val patternId: String,
    val startedAt: Long,
    val done: List<Boolean>,
    val notes: String = "",
    val counters: List<RowCounter> = emptyList(),
)
