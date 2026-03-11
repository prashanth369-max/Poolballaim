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
        val raw = computeTrajectoryNative(
            table.left,
            table.right,
            table.top,
            table.bottom,
            ballRadius,
            red.x,
            red.y,
            white.x,
            white.y,
            reflections
        )
        val ghost = Vector2(raw.getOrElse(0) { red.x }, raw.getOrElse(1) { red.y })
        val contact = Vector2(raw.getOrElse(2) { red.x }, raw.getOrElse(3) { red.y })
        val trajectory = if (raw.size > 4) raw.copyOfRange(4, raw.size) else floatArrayOf()
        return GeometryResult(trajectory, ghost, contact, floatArrayOf())
    }
}
