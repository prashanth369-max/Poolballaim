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

    private val drawingRoot = FrameLayout(context)
    private val aimView = AimView(context, appState)

    private val controlRoot = FrameLayout(context)
    private val overlayMenu = OverlayMenu(context)

    private val editRoot = FrameLayout(context)
    private val redMarker = MarkerView(context, 0xFFFF4D4D.toInt())
    private val whiteMarker = MarkerView(context, 0xFFFFFFFF.toInt())
    private val tableBoxView = TableBoxView(context, appState)

    private val inputController = InputController()
    private val markerController = MarkerController(appState, redMarker, whiteMarker)


    init {
        tableBoxView.onBoundsChanged = {
            markerController.updateRed(appState.redMarker.x, appState.redMarker.y)
            markerController.updateWhite(appState.whiteMarker.x, appState.whiteMarker.y)
            aimView.invalidate()
        }
    }
    private var drawingAttached = false
    private var controlAttached = false
    private var editAttached = false

    fun show() {
        seedTableAndMarkerDefaults()
        setupDrawingWindow()
        setupControlWindow()
        showEditWindow(false)
    }

    private fun seedTableAndMarkerDefaults() {
        val dm = context.resources.displayMetrics
        if (appState.tableBounds.right <= appState.tableBounds.left || appState.tableBounds.bottom <= appState.tableBounds.top) {
            appState.tableBounds.left = dm.widthPixels * 0.08f
            appState.tableBounds.right = dm.widthPixels * 0.92f
            appState.tableBounds.top = dm.heightPixels * 0.20f
            appState.tableBounds.bottom = dm.heightPixels * 0.72f
        }

        val centerY = (appState.tableBounds.top + appState.tableBounds.bottom) / 2f
        appState.redMarker = appState.redMarker.copy(
            x = appState.redMarker.x.coerceIn(appState.tableBounds.left + 45f, appState.tableBounds.right - 45f),
            y = appState.redMarker.y.coerceIn(appState.tableBounds.top + 45f, appState.tableBounds.bottom - 45f)
        )
        appState.whiteMarker = appState.whiteMarker.copy(
            x = appState.whiteMarker.x.coerceIn(appState.tableBounds.left + 45f, appState.tableBounds.right - 45f),
            y = appState.whiteMarker.y.coerceIn(appState.tableBounds.top + 45f, appState.tableBounds.bottom - 45f)
        )

        if (appState.redMarker.x == appState.whiteMarker.x && appState.redMarker.y == appState.whiteMarker.y) {
            appState.redMarker = appState.redMarker.copy(x = appState.tableBounds.left + 120f, y = centerY)
            appState.whiteMarker = appState.whiteMarker.copy(x = appState.tableBounds.left + 260f, y = centerY)
        }
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

        controlRoot.addView(overlayMenu, menuParams)
        // Keep menu visible by default so users can discover controls immediately.
        overlayMenu.visibility = View.VISIBLE

        overlayMenu.setOnEditModeToggleListener { enabled ->
            showEditWindow(enabled)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
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
