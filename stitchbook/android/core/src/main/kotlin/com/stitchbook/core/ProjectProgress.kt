package com.stitchbook.core

object ProjectProgress {
    /** Share of finished steps, from 0.0 to 1.0. An empty list counts as 0. */
    fun fraction(done: List<Boolean>): Float =
        if (done.isEmpty()) 0f else done.count { it }.toFloat() / done.size

    fun isFinished(done: List<Boolean>): Boolean = done.isNotEmpty() && done.all { it }
}
