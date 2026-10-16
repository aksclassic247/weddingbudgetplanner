package com.stitchbook.core

sealed interface StitchCheckResult {
    data object Match : StitchCheckResult
    /** [difference] is counted minus expected. Negative means too few stitches. */
    data class Off(val difference: Int) : StitchCheckResult
}

object StitchCheck {
    fun compare(expected: Int, counted: Int): StitchCheckResult =
        if (expected == counted) StitchCheckResult.Match else StitchCheckResult.Off(counted - expected)

    fun message(result: StitchCheckResult, expected: Int): String = when (result) {
        StitchCheckResult.Match -> "Your count matches the pattern ($expected stitches)."
        is StitchCheckResult.Off -> if (result.difference > 0) {
            "You have ${result.difference} extra. The pattern says $expected. Check for an extra increase or a missed decrease."
        } else {
            "You are ${-result.difference} short. The pattern says $expected. Check for a skipped stitch or a missed increase."
        }
    }
}
