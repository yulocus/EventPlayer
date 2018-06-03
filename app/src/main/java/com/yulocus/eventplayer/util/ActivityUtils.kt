package com.yulocus.eventplayer.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object ActivityUtils {
    fun showPermissionActivity(activity: Activity?) {
        activity?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:${it.packageName}"))
            it.startActivity(intent)
        }
    }
}
