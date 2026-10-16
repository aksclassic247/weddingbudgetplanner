package com.stitchbook.core

/**
 * One row or round counter. Immutable, so the UI just stores the latest copy.
 * [target] is optional. When it is set, [progress] shows how far along you are.
 */
data class RowCounter(
    val name: String,
    val count: Int = 0,
    val target: Int? = null,
) {
    init {
        require(name.isNotBlank()) { "A counter needs a name." }
        require(target == null || target > 0) { "A target must be more than zero." }
    }

    fun increment(): RowCounter = copy(count = count + 1)

    /** Never goes below zero, so a mis-tap cannot break the count. */
    fun decrement(): RowCounter = copy(count = maxOf(0, count - 1))

    fun reset(): RowCounter = copy(count = 0)

    /** A value from 0.0 to 1.0, or null when there is no target. */
    val progress: Float?
        get() = target?.let { minOf(1f, count.toFloat() / it) }

    val isComplete: Boolean
        get() = target != null && count >= target
}
