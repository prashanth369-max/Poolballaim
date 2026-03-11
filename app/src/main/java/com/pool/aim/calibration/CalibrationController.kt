package com.pool.aim.calibration

import com.pool.aim.state.AppState

class CalibrationController(private val appState: AppState) {
    fun setBounds(left: Float, right: Float, top: Float, bottom: Float) {
        appState.tableBounds = TableBounds(left, right, top, bottom)
    }

    fun reset() {
        appState.tableBounds = TableBounds(100f, 1800f, 200f, 1000f)
    }
}
