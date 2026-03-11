package com.pool.aim.menu

import com.pool.aim.state.AppState

class MenuController(private val appState: AppState) {
    fun setReflectionCount(value: Int) {
        appState.reflectionCount = value
    }

    fun toggleAim(enabled: Boolean) {
        appState.showAimLines = enabled
    }
}
