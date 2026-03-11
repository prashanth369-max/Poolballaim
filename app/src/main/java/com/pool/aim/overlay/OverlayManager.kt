package com.pool.aim.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import com.pool.aim.calibration.TableBoxView
import com.pool.aim.input.InputController
import com.pool.aim.marker.MarkerController
import com.pool.aim.marker.MarkerView
import com.pool.aim.menu.OverlayMenu
import com.pool.aim.renderer.AimView
import com.pool.aim.state.AppState

class OverlayManager(private val context: Context, private val appState: AppState) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val root = FrameLayout(context)
    private val aimView = AimView(context, appState)
    private val redMarker = MarkerView(context, 0xFFFF4D4D.toInt())
    private val whiteMarker = MarkerView(context, 0xFFFFFFFF.toInt())
    private val tableBoxView = TableBoxView(context, appState)
    private val overlayMenu = OverlayMenu(context)
    private val overlayButton = OverlayButton(context)

    private val inputController = InputController()
    private val markerController = MarkerController(appState, redMarker, whiteMarker)

    fun show() {
        root.removeAllViews()
        root.addView(aimView)
        root.addView(redMarker)
        root.addView(whiteMarker)
        root.addView(tableBoxView)
        root.addView(overlayMenu)
        root.addView(overlayButton)

        inputController.bindMarkerInteraction(root, markerController, aimView)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }

        windowManager.addView(root, params)
    }

    fun hide() {
        runCatching { windowManager.removeView(root) }
    }
}
