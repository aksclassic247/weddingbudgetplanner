package com.stitchbook.core

/**
 * Works out where to place increases or decreases so they are spread evenly
 * across a row. This is plain arithmetic, so the answer is the same every time.
 */
enum class ShapingType(val label: String, val operation: String, val stitchesUsed: Int, val stitchChange: Int) {
    INCREASE(label = "Increase", operation = "inc", stitchesUsed = 1, stitchChange = 1),
    DECREASE(label = "Decrease", operation = "k2tog", stitchesUsed = 2, stitchChange = -1),
}

/** Knit [plainStitches] then do the shaping stitch, repeated [repeats] times. */
data class ShapingRun(val plainStitches: Int, val repeats: Int)

data class ShapingPlan(
    val type: ShapingType,
    val stitchesBefore: Int,
    val stitchesAfter: Int,
    val runs: List<ShapingRun>,
) {
    /** A written version of the plan, such as "k9, inc (repeat 6 times)". */
    fun describe(): String = runs.joinToString(separator = ", then ") { run ->
        val op = if (run.plainStitches == 0) {
            type.operation
        } else {
            "k${run.plainStitches}, ${type.operation}"
        }
        if (run.repeats == 1) op else "$op (repeat ${run.repeats} times)"
    }
}

object ShapingCalculator {

    /**
     * @param stitches stitches on the needle or hook before the shaping row
     * @param changes how many increases or decreases to make
     * @throws IllegalArgumentException when the numbers cannot work, for example
     *   removing more stitches than the row has.
     */
    fun plan(stitches: Int, changes: Int, type: ShapingType): ShapingPlan {
        require(stitches > 0) { "Stitch count must be more than zero." }
        require(changes > 0) { "Number of ${type.label.lowercase()}s must be more than zero." }

        val plain = stitches - type.stitchesUsed * changes
        require(plain >= 0) {
            "A row with $stitches stitches cannot fit $changes ${type.label.lowercase()}s."
        }

        val base = plain / changes
        val extra = plain % changes

        // The first `extra` repeats get one more plain stitch, so the spacing stays even.
        val perRepeat = List(changes) { index -> if (index < extra) base + 1 else base }
        val runs = perRepeat.fold(mutableListOf<ShapingRun>()) { acc, plainCount ->
            val last = acc.lastOrNull()
            if (last != null && last.plainStitches == plainCount) {
                acc[acc.lastIndex] = last.copy(repeats = last.repeats + 1)
            } else {
                acc.add(ShapingRun(plainStitches = plainCount, repeats = 1))
            }
            acc
        }

        return ShapingPlan(
            type = type,
            stitchesBefore = stitches,
            stitchesAfter = stitches + type.stitchChange * changes,
            runs = runs,
        )
    }
}
