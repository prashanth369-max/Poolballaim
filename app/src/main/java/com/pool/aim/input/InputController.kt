package com.pool.aim.input

import android.view.View
import android.view.ViewGroup
import com.pool.aim.marker.MarkerController
import com.pool.aim.renderer.AimView

class InputController {
    fun bindMarkerInteraction(root: ViewGroup, markerController: MarkerController, aimView: AimView) {
        val red = markerController.redView
        val white = markerController.whiteView

        red.setOnTouchListener { _, e ->
            val handled = GestureHandler { x, y -> markerController.updateRed(x, y); aimView.invalidate() }.onTouch(e)
            handled
        }
        white.setOnTouchListener { _, e ->
            val handled = GestureHandler { x, y -> markerController.updateWhite(x, y); aimView.invalidate() }.onTouch(e)
            handled
        }

        root.setOnTouchListener { _: View, _ -> false }
    }
}
