package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ShapingCalculatorTest {

    @Test
    fun evenIncreasesUseOneRepeat() {
        val plan = ShapingCalculator.plan(stitches = 60, changes = 6, type = ShapingType.INCREASE)
        assertEquals(66, plan.stitchesAfter)
        assertEquals(listOf(ShapingRun(9, 6)), plan.runs)
        assertEquals("k9, inc (repeat 6 times)", plan.describe())
    }

    @Test
    fun unevenIncreasesSpreadTheExtraStitchesToTheFirstRepeats() {
        // 10 stitches, 3 increases: 7 plain stitches left, so 3, 2, 2.
        val plan = ShapingCalculator.plan(stitches = 10, changes = 3, type = ShapingType.INCREASE)
        assertEquals(13, plan.stitchesAfter)
        assertEquals(listOf(ShapingRun(3, 1), ShapingRun(2, 2)), plan.runs)
        assertEquals("k3, inc, then k2, inc (repeat 2 times)", plan.describe())
    }

    @Test
    fun decreasesRemoveTwoStitchesEachTime() {
        // 72 stitches, 8 decreases: 56 plain, so 7 per repeat.
        val plan = ShapingCalculator.plan(stitches = 72, changes = 8, type = ShapingType.DECREASE)
        assertEquals(64, plan.stitchesAfter)
        assertEquals("k7, k2tog (repeat 8 times)", plan.describe())
    }

    @Test
    fun hatCrownRoundsMatchTheShapingMath() {
        // Each crown round in the Ribbed Knit Hat: 80 -> 72 -> 64 ... -> 8.
        var stitches = 80
        val expectedAfter = listOf(72, 64, 56, 48, 40, 32, 24, 16)
        for (after in expectedAfter) {
            val changes = (stitches - after) / 1
            val plan = ShapingCalculator.plan(stitches, changes, ShapingType.DECREASE)
            assertEquals(after, plan.stitchesAfter)
            stitches = after
        }
    }

    @Test
    fun zeroPlainStitchesShowsOnlyTheShapingStitch() {
        // 16 stitches, 8 decreases: every stitch goes into a k2tog.
        val plan = ShapingCalculator.plan(stitches = 16, changes = 8, type = ShapingType.DECREASE)
        assertEquals(8, plan.stitchesAfter)
        assertEquals("k2tog (repeat 8 times)", plan.describe())
    }

    @Test
    fun rejectsImpossibleRows() {
        assertFailsWith<IllegalArgumentException> {
            ShapingCalculator.plan(stitches = 10, changes = 6, type = ShapingType.DECREASE)
        }
        assertFailsWith<IllegalArgumentException> {
            ShapingCalculator.plan(stitches = 0, changes = 1, type = ShapingType.INCREASE)
        }
        assertFailsWith<IllegalArgumentException> {
            ShapingCalculator.plan(stitches = 10, changes = 0, type = ShapingType.INCREASE)
        }
    }
}
