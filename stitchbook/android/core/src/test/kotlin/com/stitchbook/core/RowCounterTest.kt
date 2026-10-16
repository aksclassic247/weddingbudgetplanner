package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RowCounterTest {

    @Test
    fun incrementAndDecrementNeverGoBelowZero() {
        val counter = RowCounter("Rows").decrement().decrement()
        assertEquals(0, counter.count)
        assertEquals(1, counter.increment().count)
    }

    @Test
    fun progressUsesTheTargetAndCapsAtOne() {
        var counter = RowCounter("Rows", target = 4)
        assertEquals(0f, counter.progress)
        repeat(2) { counter = counter.increment() }
        assertEquals(0.5f, counter.progress)
        assertFalse(counter.isComplete)
        repeat(5) { counter = counter.increment() }
        assertEquals(1f, counter.progress)
        assertTrue(counter.isComplete)
    }

    @Test
    fun countersWithoutTargetHaveNoProgress() {
        assertNull(RowCounter("Repeats").increment().progress)
    }

    @Test
    fun undoReversesTheLastChange() {
        val counter = RowCounter("Rows", count = 5).increment()
        assertEquals(6, counter.count)
        assertEquals(5, counter.undo().count)
    }

    @Test
    fun undoOnlyRemembersOneStep() {
        val counter = RowCounter("Rows").increment().increment().increment()
        val undone = counter.undo()
        assertEquals(2, undone.count)
        assertNull(undone.undo().previousCount)
        assertEquals(2, undone.undo().count)
    }

    @Test
    fun undoAfterResetReturnsTheOldCount() {
        val counter = RowCounter("Rows", count = 7).reset()
        assertEquals(0, counter.count)
        assertEquals(7, counter.undo().count)
    }

    @Test
    fun tapAtZeroIsNotRecorded() {
        val counter = RowCounter("Rows").decrement()
        assertNull(counter.previousCount)
    }
}
