package com.stitchbook.core

import kotlin.math.ceil

data class YarnEstimate(val metres: Double, val balls: Int)

/**
 * Estimates how much yarn a project needs from the weight and length on the label,
 * for example "50 g / 100 m". Adds a safety margin, then rounds the ball count up.
 */
object YarnCalculator {
    fun estimate(
        projectGrams: Double,
        ballGrams: Double,
        ballMetres: Double,
        marginPercent: Double = 10.0,
    ): YarnEstimate {
        require(projectGrams > 0) { "Project weight must be more than zero." }
        require(ballGrams > 0) { "Ball weight must be more than zero." }
        require(ballMetres > 0) { "Ball length must be more than zero." }
        require(marginPercent >= 0) { "Margin cannot be negative." }

        val gramsPerMetre = ballGrams / ballMetres
        val metres = projectGrams / gramsPerMetre * (1 + marginPercent / 100)
        return YarnEstimate(metres = metres, balls = ceil(metres / ballMetres).toInt())
    }
}
