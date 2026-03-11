package com.pool.aim.control

object Helper {
    fun clamp(value: Float, min: Float, max: Float): Float = value.coerceIn(min, max)
}
