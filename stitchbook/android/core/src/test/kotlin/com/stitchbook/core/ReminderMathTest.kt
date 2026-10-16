package com.stitchbook.core

import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReminderMathTest {
    private val zone = ZoneId.of("Europe/London")

    @Test
    fun laterTodayFiresToday() {
        val now = ZonedDateTime.of(2026, 10, 16, 9, 30, 0, 0, zone)
        assertEquals(ZonedDateTime.of(2026, 10, 16, 19, 0, 0, 0, zone), ReminderMath.nextOccurrence(now, 19, 0))
    }

    @Test
    fun passedTodayFiresTomorrow() {
        val now = ZonedDateTime.of(2026, 10, 16, 20, 0, 0, 0, zone)
        assertEquals(ZonedDateTime.of(2026, 10, 17, 19, 0, 0, 0, zone), ReminderMath.nextOccurrence(now, 19, 0))
    }

    @Test
    fun exactlyNowFiresTomorrow() {
        val now = ZonedDateTime.of(2026, 10, 16, 19, 0, 0, 0, zone)
        assertEquals(ZonedDateTime.of(2026, 10, 17, 19, 0, 0, 0, zone), ReminderMath.nextOccurrence(now, 19, 0))
    }

    @Test
    fun validatesTimes() {
        assertTrue(ReminderMath.isValidTime(0, 0))
        assertTrue(ReminderMath.isValidTime(23, 59))
        assertFalse(ReminderMath.isValidTime(24, 0))
        assertFalse(ReminderMath.isValidTime(10, 60))
        assertFailsWith<IllegalArgumentException> { ReminderMath.nextOccurrence(ZonedDateTime.now(zone), 25, 0) }
    }
}
