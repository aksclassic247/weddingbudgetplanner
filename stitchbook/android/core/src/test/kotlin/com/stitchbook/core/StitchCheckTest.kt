package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals

class StitchCheckTest {

    @Test
    fun reportsMatchAndDifference() {
        assertEquals(StitchCheckResult.Match, StitchCheck.compare(12, 12))
        assertEquals(StitchCheckResult.Off(-2), StitchCheck.compare(12, 10))
        assertEquals(StitchCheckResult.Off(3), StitchCheck.compare(12, 15))
    }

    @Test
    fun messageTellsYouWhichWayToLook() {
        assertEquals(
            "You are 2 short. The pattern says 12. Check for a skipped stitch or a missed increase.",
            StitchCheck.message(StitchCheckResult.Off(-2), 12),
        )
    }
}
