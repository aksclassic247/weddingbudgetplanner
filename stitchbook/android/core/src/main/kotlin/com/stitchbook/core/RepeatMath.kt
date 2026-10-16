package com.stitchbook.core

/**
 * A repeat counter follows another counter. When a pattern repeats every N rows,
 * it counts how many full repeats the row counter has reached.
 */
object RepeatMath {
    /** Full repeats finished after [rows] rows. */
    fun completedRepeats(rows: Int, rowsPerRepeat: Int): Int {
        require(rowsPerRepeat > 0) { "A repeat needs at least one row." }
        require(rows >= 0) { "Rows cannot be negative." }
        return rows / rowsPerRepeat
    }

    /** Rows done inside the repeat that is currently in progress. */
    fun rowsIntoCurrentRepeat(rows: Int, rowsPerRepeat: Int): Int {
        require(rowsPerRepeat > 0) { "A repeat needs at least one row." }
        require(rows >= 0) { "Rows cannot be negative." }
        return rows % rowsPerRepeat
    }
}
