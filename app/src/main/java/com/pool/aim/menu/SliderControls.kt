package com.pool.aim.menu

import android.content.Context
import android.widget.LinearLayout
import com.google.android.material.slider.Slider

class SliderControls(context: Context) : LinearLayout(context) {
    val reflectionSlider = Slider(context).apply {
        valueFrom = 0f
        valueTo = 5f
        stepSize = 1f
        value = 2f
    }

    init {
        orientation = VERTICAL
        addView(reflectionSlider)
    }
}
