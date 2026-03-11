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
        appState.redMarker = Vector2(x, y)
        redView.x = x - redView.width / 2f
        redView.y = y - redView.height / 2f
    }

    fun updateWhite(x: Float, y: Float) {
        if (appState.markerLocked) return
        appState.whiteMarker = Vector2(x, y)
        whiteView.x = x - whiteView.width / 2f
        whiteView.y = y - whiteView.height / 2f
    }
}
