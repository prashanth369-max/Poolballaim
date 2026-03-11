package com.pool.aim.utils

import android.content.Context
import android.util.TypedValue

object ScreenUtils {
    fun dp(context: Context, value: Float): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        context.resources.displayMetrics
    )
}
