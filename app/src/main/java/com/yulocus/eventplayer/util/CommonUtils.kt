package com.yulocus.eventplayer.util

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager

object CommonUtils {
    fun dpToPx(context: Context, dp: Int): Int = Math.round(dp * getPixelScaleFactor(context))

    fun pxToDp(context: Context, px: Int): Int = Math.round(px / getPixelScaleFactor(context))

    private fun getPixelScaleFactor(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
    }

    fun getScreenSize(context: Context): IntArray {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val screen = IntArray(2)
        screen[0] = size.x
        screen[1] = size.y
        return screen
    }
}