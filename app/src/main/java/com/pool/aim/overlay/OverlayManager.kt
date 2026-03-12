package com.pool.aim.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.pool.aim.calibration.TableBoxView
import com.pool.aim.input.InputController
import com.pool.aim.marker.MarkerController
import com.pool.aim.marker.MarkerView
import com.pool.aim.menu.OverlayMenu
import com.pool.aim.renderer.AimView
import com.pool.aim.state.AppState
import com.pool.aim.utils.ScreenUtils

class OverlayManager(private val context: Context, private val appState: AppState) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    // Drawing window: full-screen but touch-pass-through.
    private val drawingRoot = FrameLayout(context)
    private val aimView = AimView(context, appState)

    // Control window: small touchable panel for menu + floating button.
    private val controlRoot = FrameLayout(context)
    private val overlayMenu = OverlayMenu(context)
    private val overlayButton = OverlayButton(context)

    // Edit window: full-screen touch layer shown only when marker edit mode is enabled.
    private val editRoot = FrameLayout(context)
    private val redMarker = MarkerView(context, 0xFFFF4D4D.toInt())
    private val whiteMarker = MarkerView(context, 0xFFFFFFFF.toInt())
    private val tableBoxView = TableBoxView(context, appState)

    private val inputController = InputController()
    private val markerController = MarkerController(appState, redMarker, whiteMarker)

    private var drawingAttached = false
    private var controlAttached = false
    private var editAttached = false

    fun show() {
        setupDrawingWindow()
        setupControlWindow()
        showEditWindow(false)
    }

    private fun setupDrawingWindow() {
        drawingRoot.removeAllViews()
        drawingRoot.addView(
            aimView,
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }

        if (!drawingAttached) {
            windowManager.addView(drawingRoot, params)
            drawingAttached = true
        }
    }

    private fun setupControlWindow() {
        controlRoot.removeAllViews()

        val menuParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.TOP or Gravity.END
        ).apply {
            topMargin = ScreenUtils.dp(context, 16f).toInt()
            marginEnd = ScreenUtils.dp(context, 16f).toInt()
        }

        val buttonSize = ScreenUtils.dp(context, 56f).toInt()
        val buttonParams = FrameLayout.LayoutParams(buttonSize, buttonSize, Gravity.TOP or Gravity.START).apply {
            topMargin = ScreenUtils.dp(context, 16f).toInt()
            marginStart = ScreenUtils.dp(context, 16f).toInt()
        }

        controlRoot.addView(overlayMenu, menuParams)
        controlRoot.addView(overlayButton, buttonParams)

        overlayMenu.visibility = View.GONE
        overlayButton.setOnClickListener {
            overlayMenu.visibility = if (overlayMenu.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        overlayMenu.setOnEditModeToggleListener { enabled ->
            showEditWindow(enabled)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }

        if (!controlAttached) {
            windowManager.addView(controlRoot, params)
            controlAttached = true
        }
    }

    private fun showEditWindow(enabled: Boolean) {
        if (enabled) {
            if (!editAttached) {
                editRoot.removeAllViews()
                editRoot.addView(
                    tableBoxView,
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
                editRoot.addView(redMarker)
                editRoot.addView(whiteMarker)

                inputController.bindMarkerInteraction(editRoot, markerController, aimView)
                positionMarkersFromState()

                val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    windowType(),
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT
                ).apply { gravity = Gravity.TOP or Gravity.START }

                windowManager.addView(editRoot, params)
                editAttached = true
            }
        } else {
            if (editAttached) {
                runCatching { windowManager.removeView(editRoot) }
                editAttached = false
            }
        }
    }

    private fun positionMarkersFromState() {
        redMarker.post {
            markerController.updateRed(appState.redMarker.x, appState.redMarker.y)
            markerController.updateWhite(appState.whiteMarker.x, appState.whiteMarker.y)
            aimView.invalidate()
        }
    }

    fun hide() {
        if (editAttached) runCatching { windowManager.removeView(editRoot) }
        if (controlAttached) runCatching { windowManager.removeView(controlRoot) }
        if (drawingAttached) runCatching { windowManager.removeView(drawingRoot) }
        editAttached = false
        controlAttached = false
        drawingAttached = false
    }

    private fun windowType(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
}
