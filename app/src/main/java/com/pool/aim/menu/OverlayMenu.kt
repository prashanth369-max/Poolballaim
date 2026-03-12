package com.pool.aim.menu

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.pool.aim.R

class OverlayMenu(context: Context) : FrameLayout(context) {
    private var editModeEnabled = false
    private var editModeToggleListener: ((Boolean) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.overlay_menu, this, true)
        alpha = 0.95f

        val editButton = findViewById<Button>(R.id.btnEditMarkers)
        val hideMenuButton = findViewById<Button>(R.id.btnHideMenu)

        editButton.setOnClickListener {
            editModeEnabled = !editModeEnabled
            editButton.text = if (editModeEnabled) {
                context.getString(R.string.disable_edit_mode)
            } else {
                context.getString(R.string.enable_edit_mode)
            }
            editModeToggleListener?.invoke(editModeEnabled)
        }

        hideMenuButton.setOnClickListener {
            visibility = GONE
        }
    }

    fun setOnEditModeToggleListener(listener: (Boolean) -> Unit) {
        editModeToggleListener = listener
    }
}
