package com.stitchbook.core

import java.time.ZonedDateTime

/** Works out when a daily reminder should next fire. */
object ReminderMath {
    fun isValidTime(hour: Int, minute: Int): Boolean = hour in 0..23 && minute in 0..59

    /**
     * The next time at [hour]:[minute] that is strictly after [now].
     * If today's time has already passed, this returns tomorrow at that time.
     */
    fun nextOccurrence(now: ZonedDateTime, hour: Int, minute: Int): ZonedDateTime {
        require(isValidTime(hour, minute)) { "Reminder time must be between 00:00 and 23:59." }
        val today = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        return if (today.isAfter(now)) today else today.plusDays(1)
    }
}
