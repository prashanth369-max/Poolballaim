package com.pool.aim.marker

import com.pool.aim.geometry.Vector2
import com.pool.aim.state.AppState

class MarkerController(
    private val appState: AppState,
    val redView: MarkerView,
    val whiteView: MarkerView
) {
    fun updateRed(x: Float, y: Float) {
        if (appState.markerLocked) return
        val p = clampToTable(x, y)
        appState.redMarker = p
        redView.x = p.x - redView.width / 2f
        redView.y = p.y - redView.height / 2f
    }

    fun updateWhite(x: Float, y: Float) {
        if (appState.markerLocked) return
        val p = clampToTable(x, y)
        appState.whiteMarker = p
        whiteView.x = p.x - whiteView.width / 2f
        whiteView.y = p.y - whiteView.height / 2f
    }

    private fun clampToTable(x: Float, y: Float): Vector2 {
        val bounds = appState.tableBounds
        val horizontalPadding = redView.width / 2f
        val verticalPadding = redView.height / 2f

        val clampedX = x.coerceIn(bounds.left + horizontalPadding, bounds.right - horizontalPadding)
        val clampedY = y.coerceIn(bounds.top + verticalPadding, bounds.bottom - verticalPadding)
        return Vector2(clampedX, clampedY)
    }
}
