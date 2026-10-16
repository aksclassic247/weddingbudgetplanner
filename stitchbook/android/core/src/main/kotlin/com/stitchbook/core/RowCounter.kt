package com.stitchbook.core

/**
 * One row or round counter. Immutable, so the UI just stores the latest copy.
 * [target] is optional. When it is set, [progress] shows how far along you are.
 * [previousCount] remembers the count before the last change, so [undo] can reverse one step.
 */
data class RowCounter(
    val name: String,
    val count: Int = 0,
    val target: Int? = null,
    val id: String = java.util.UUID.randomUUID().toString(),
    val previousCount: Int? = null,
) {
    init {
        require(name.isNotBlank()) { "A counter needs a name." }
        require(target == null || target > 0) { "A target must be more than zero." }
    }

    fun increment(): RowCounter = copy(count = count + 1, previousCount = count)

    /** Never goes below zero, so a mis-tap cannot break the count. A tap at zero is not recorded. */
    fun decrement(): RowCounter = if (count == 0) this else copy(count = count - 1, previousCount = count)

    fun reset(): RowCounter = if (count == 0) this else copy(count = 0, previousCount = count)

    /** Reverses the last increment, decrement or reset. Only one step is remembered. */
    fun undo(): RowCounter = previousCount?.let { copy(count = it, previousCount = null) } ?: this

    /** A value from 0.0 to 1.0, or null when there is no target. */
    val progress: Float?
        get() = target?.let { minOf(1f, count.toFloat() / it) }

    val isComplete: Boolean
        get() = target != null && count >= target
}
