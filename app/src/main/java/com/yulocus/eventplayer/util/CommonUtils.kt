package com.yulocus.eventplayer.util

import android.content.Context
import android.util.DisplayMetrics

object CommonUtils {
    fun dpToPx(context: Context, dp: Int): Int = Math.round(dp * getPixelScaleFactor(context))

    fun pxToDp(context: Context, px: Int): Int = Math.round(px / getPixelScaleFactor(context))

    private fun getPixelScaleFactor(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
    }

}