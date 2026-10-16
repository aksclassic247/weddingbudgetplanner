package com.stitchbook.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProjectProgressTest {
    @Test
    fun fractionCountsFinishedSteps() {
        assertEquals(0.5f, ProjectProgress.fraction(listOf(true, false)))
        assertEquals(0f, ProjectProgress.fraction(emptyList()))
    }

    @Test
    fun finishedOnlyWhenEveryStepIsDone() {
        assertFalse(ProjectProgress.isFinished(listOf(true, false)))
        assertTrue(ProjectProgress.isFinished(listOf(true, true)))
        assertFalse(ProjectProgress.isFinished(emptyList()))
    }
}
