package com.pool.aim.geometry

import com.pool.aim.calibration.TableBounds

data class GeometryResult(
    val trajectoryPoints: FloatArray,
    val ghostBall: Vector2,
    val contactPoint: Vector2,
    val bankPoints: FloatArray
)

object GeometryBridge {
    init {
        load()
    }

    fun load() {
        runCatching { System.loadLibrary("native-lib") }
    }

    external fun computeTrajectoryNative(
        tableLeft: Float,
        tableRight: Float,
        tableTop: Float,
        tableBottom: Float,
        ballRadius: Float,
        redMarkerX: Float,
        redMarkerY: Float,
        whiteMarkerX: Float,
        whiteMarkerY: Float,
        reflectionCount: Int
    ): FloatArray

    fun compute(table: TableBounds, ballRadius: Float, red: Vector2, white: Vector2, reflections: Int): GeometryResult {
        val clampedRed = clampToTable(red, table, ballRadius)
        val clampedWhite = clampToTable(white, table, ballRadius)

        val raw = computeTrajectoryNative(
            table.left,
            table.right,
            table.top,
            table.bottom,
            ballRadius,
            clampedRed.x,
            clampedRed.y,
            clampedWhite.x,
            clampedWhite.y,
            reflections
        )
        val ghost = Vector2(raw.getOrElse(0) { clampedRed.x }, raw.getOrElse(1) { clampedRed.y })
        val contact = Vector2(raw.getOrElse(2) { clampedRed.x }, raw.getOrElse(3) { clampedRed.y })
        val trajectory = if (raw.size > 4) raw.copyOfRange(4, raw.size) else floatArrayOf()
        return GeometryResult(trajectory, ghost, contact, floatArrayOf())
    }

    private fun clampToTable(point: Vector2, table: TableBounds, padding: Float): Vector2 {
        val x = point.x.coerceIn(table.left + padding, table.right - padding)
        val y = point.y.coerceIn(table.top + padding, table.bottom - padding)
        return Vector2(x, y)
    }
}
