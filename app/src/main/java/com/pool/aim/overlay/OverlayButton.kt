package com.pool.aim.overlay

import android.content.Context
import androidx.appcompat.widget.AppCompatImageButton
import com.pool.aim.R

class OverlayButton(context: Context) : AppCompatImageButton(context) {
    init {
        setImageResource(android.R.drawable.ic_menu_manage)
        setBackgroundResource(R.drawable.floating_button)
    }
}
