package com.pool.aim.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.pool.aim.geometry.GeometryBridge
import com.pool.aim.state.AppState
import com.pool.aim.utils.Constants

class AimView(context: Context, private val state: AppState) : View(context) {
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(180, 85, 184, 255)
        strokeWidth = Constants.DEFAULT_LINE_THICKNESS
    }
    private val ghostPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(160, 255, 255, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private val pocketPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(120, 255, 214, 10)
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val trajectoryRenderer = TrajectoryRenderer(linePaint)
    private val ghostRenderer = GhostBallRenderer(ghostPaint, Constants.DEFAULT_BALL_RADIUS)
    private val pocketRenderer = PocketRenderer(pocketPaint, 22f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val result = GeometryBridge.compute(
            state.tableBounds,
            Constants.DEFAULT_BALL_RADIUS,
            state.redMarker,
            state.whiteMarker,
            state.reflectionCount
        )

        if (state.showAimLines) trajectoryRenderer.draw(canvas, result.trajectoryPoints)
        ghostRenderer.draw(canvas, result.ghostBall)
        if (state.showPockets) pocketRenderer.draw(canvas, state.tableBounds)
    }
}
