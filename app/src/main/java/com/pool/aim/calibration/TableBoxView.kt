package com.pool.aim.calibration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.pool.aim.state.AppState
import kotlin.math.abs

class TableBoxView(context: Context, private val appState: AppState) : View(context) {
    private val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(180, 66, 245, 162)
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(220, 66, 245, 162)
        style = Paint.Style.FILL
    }

    private var activeHandle = Handle.NONE
    private val handleRadius = 22f

    var onBoundsChanged: ((TableBounds) -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val b = appState.tableBounds
        canvas.drawRect(b.left, b.top, b.right, b.bottom, border)
        canvas.drawCircle(b.left, b.top, handleRadius, handlePaint)
        canvas.drawCircle(b.right, b.top, handleRadius, handlePaint)
        canvas.drawCircle(b.left, b.bottom, handleRadius, handlePaint)
        canvas.drawCircle(b.right, b.bottom, handleRadius, handlePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val b = appState.tableBounds
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                activeHandle = detectHandle(event.x, event.y, b)
                return activeHandle != Handle.NONE
            }

            MotionEvent.ACTION_MOVE -> {
                if (activeHandle == Handle.NONE) return false
                when (activeHandle) {
                    Handle.TOP_LEFT -> {
                        b.left = event.x.coerceAtMost(b.right - 150f).coerceAtLeast(0f)
                        b.top = event.y.coerceAtMost(b.bottom - 150f).coerceAtLeast(0f)
                    }

                    Handle.TOP_RIGHT -> {
                        b.right = event.x.coerceAtLeast(b.left + 150f).coerceAtMost(width.toFloat())
                        b.top = event.y.coerceAtMost(b.bottom - 150f).coerceAtLeast(0f)
                    }

                    Handle.BOTTOM_LEFT -> {
                        b.left = event.x.coerceAtMost(b.right - 150f).coerceAtLeast(0f)
                        b.bottom = event.y.coerceAtLeast(b.top + 150f).coerceAtMost(height.toFloat())
                    }

                    Handle.BOTTOM_RIGHT -> {
                        b.right = event.x.coerceAtLeast(b.left + 150f).coerceAtMost(width.toFloat())
                        b.bottom = event.y.coerceAtLeast(b.top + 150f).coerceAtMost(height.toFloat())
                    }

                    Handle.NONE -> Unit
                }
                onBoundsChanged?.invoke(b)
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activeHandle = Handle.NONE
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun detectHandle(x: Float, y: Float, b: TableBounds): Handle {
        return when {
            isNear(x, y, b.left, b.top) -> Handle.TOP_LEFT
            isNear(x, y, b.right, b.top) -> Handle.TOP_RIGHT
            isNear(x, y, b.left, b.bottom) -> Handle.BOTTOM_LEFT
            isNear(x, y, b.right, b.bottom) -> Handle.BOTTOM_RIGHT
            else -> Handle.NONE
        }
    }

    private fun isNear(x: Float, y: Float, hx: Float, hy: Float): Boolean {
        return abs(x - hx) <= handleRadius * 1.8f && abs(y - hy) <= handleRadius * 1.8f
    }

    private enum class Handle {
        NONE,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}
