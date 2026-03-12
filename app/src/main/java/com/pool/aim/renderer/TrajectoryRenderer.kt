package com.pool.aim.renderer

import android.graphics.Canvas
import android.graphics.Paint

class TrajectoryRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, points: FloatArray) {
        if (points.size < 4) return
        var i = 0
        while (i + 3 < points.size) {
            canvas.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3], paint)
            i += 2
        }
    }
}
