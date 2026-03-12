package com.pool.aim.overlay

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import com.pool.aim.R

class OverlayButton(context: Context) : AppCompatImageButton(context) {
    private val targetSizePx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        56f,
        context.resources.displayMetrics
    ).toInt()

    init {
        setImageResource(android.R.drawable.ic_menu_manage)
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        adjustViewBounds = false
        setPadding(16, 16, 16, 16)
        minimumWidth = targetSizePx
        minimumHeight = targetSizePx
        maxWidth = targetSizePx
        maxHeight = targetSizePx
        setBackgroundResource(R.drawable.floating_button)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(targetSizePx, targetSizePx)
    }
}
