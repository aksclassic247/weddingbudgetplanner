package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class YarnCalculatorTest {

    @Test
    fun addsMarginAndRoundsBallsUp() {
        // 120 g from a 50 g / 100 m ball is 240 m, plus 10 percent is 264 m, which needs 3 balls.
        val estimate = YarnCalculator.estimate(projectGrams = 120.0, ballGrams = 50.0, ballMetres = 100.0)
        assertEquals(264.0, estimate.metres, absoluteTolerance = 1e-9)
        assertEquals(3, estimate.balls)
    }

    @Test
    fun exactFitDoesNotAddAnExtraBall() {
        val estimate = YarnCalculator.estimate(projectGrams = 100.0, ballGrams = 50.0, ballMetres = 100.0, marginPercent = 0.0)
        assertEquals(200.0, estimate.metres, absoluteTolerance = 1e-9)
        assertEquals(2, estimate.balls)
    }

    @Test
    fun rejectsBadInput() {
        assertFailsWith<IllegalArgumentException> {
            YarnCalculator.estimate(projectGrams = 0.0, ballGrams = 50.0, ballMetres = 100.0)
        }
        assertFailsWith<IllegalArgumentException> {
            YarnCalculator.estimate(projectGrams = 10.0, ballGrams = 50.0, ballMetres = 100.0, marginPercent = -1.0)
        }
    }
}
