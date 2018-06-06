package com.yulocus.eventplayer.util

import android.app.Activity
import android.view.View
import android.view.Window
import android.view.WindowManager

object StatusBarUtils {

    fun hideStatusBar(activity: Activity?) {
        if (activity == null) return
        val window = activity.window
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}