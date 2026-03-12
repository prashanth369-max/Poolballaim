package com.pool.aim.overlay

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import com.pool.aim.R

class OverlayButton(context: Context) : AppCompatImageButton(context) {
    init {
        setImageResource(android.R.drawable.ic_menu_manage)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setPadding(16, 16, 16, 16)
        setBackgroundResource(R.drawable.floating_button)
    }
}
