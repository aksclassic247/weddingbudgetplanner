package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RepeatMathTest {
    @Test
    fun countsFullRepeats() {
        assertEquals(0, RepeatMath.completedRepeats(rows = 7, rowsPerRepeat = 8))
        assertEquals(1, RepeatMath.completedRepeats(rows = 8, rowsPerRepeat = 8))
        assertEquals(3, RepeatMath.completedRepeats(rows = 26, rowsPerRepeat = 8))
    }

    @Test
    fun reportsRowsIntoCurrentRepeat() {
        assertEquals(2, RepeatMath.rowsIntoCurrentRepeat(rows = 26, rowsPerRepeat = 8))
        assertEquals(0, RepeatMath.rowsIntoCurrentRepeat(rows = 24, rowsPerRepeat = 8))
    }

    @Test
    fun rejectsBadInput() {
        assertFailsWith<IllegalArgumentException> { RepeatMath.completedRepeats(rows = 3, rowsPerRepeat = 0) }
        assertFailsWith<IllegalArgumentException> { RepeatMath.completedRepeats(rows = -1, rowsPerRepeat = 4) }
    }
}
