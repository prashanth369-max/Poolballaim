package com.pool.aim.renderer

import android.graphics.Canvas
import android.graphics.Paint
import com.pool.aim.geometry.Vector2

class GhostBallRenderer(private val paint: Paint, private val radius: Float) {
    fun draw(canvas: Canvas, ghost: Vector2) {
        canvas.drawCircle(ghost.x, ghost.y, radius, paint)
    }
}
