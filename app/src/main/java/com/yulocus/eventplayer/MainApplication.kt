package com.yulocus.eventplayer

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import timber.log.Timber
import kotlin.properties.Delegates

class MainApplication : MultiDexApplication() {

    companion object {
        var instance by Delegates.notNull<MainApplication>()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // setup timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.d("[start app]")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}