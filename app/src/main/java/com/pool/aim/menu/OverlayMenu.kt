package com.pool.aim.menu

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.pool.aim.R

class OverlayMenu(context: Context) : FrameLayout(context) {
    init {
        LayoutInflater.from(context).inflate(R.layout.overlay_menu, this, true)
        alpha = 0.9f
    }
}
