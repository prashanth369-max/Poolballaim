package com.pool.aim.renderer

import android.graphics.Canvas
import android.graphics.Paint
import com.pool.aim.calibration.TableBounds

class PocketRenderer(private val paint: Paint, private val pocketRadius: Float) {
    fun draw(canvas: Canvas, b: TableBounds) {
        val mid = (b.left + b.right) / 2f
        canvas.drawCircle(b.left, b.top, pocketRadius, paint)
        canvas.drawCircle(b.right, b.top, pocketRadius, paint)
        canvas.drawCircle(b.left, b.bottom, pocketRadius, paint)
        canvas.drawCircle(b.right, b.bottom, pocketRadius, paint)
        canvas.drawCircle(mid, b.top, pocketRadius, paint)
        canvas.drawCircle(mid, b.bottom, pocketRadius, paint)
    }
}
