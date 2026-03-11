package com.pool.aim.calibration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.pool.aim.state.AppState

class TableBoxView(context: Context, private val appState: AppState) : View(context) {
    private val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(160, 66, 245, 162)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val b = appState.tableBounds
        canvas.drawRect(b.left, b.top, b.right, b.bottom, border)
    }
}
