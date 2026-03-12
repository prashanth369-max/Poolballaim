package com.pool.aim.input

import android.view.MotionEvent

typealias DragListener = (x: Float, y: Float) -> Unit

class GestureHandler(private val onDrag: DragListener) {
    fun onTouch(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_MOVE || event.actionMasked == MotionEvent.ACTION_DOWN) {
            onDrag(event.rawX, event.rawY)
            return true
        }
        return false
    }
}
