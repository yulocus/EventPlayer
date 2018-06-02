package com.yulocus.eventplayer.base

import android.content.Context

interface MvpView {

    fun getContext(): Context

    fun showLoading(isLoading: Boolean)

    fun showError(message: String)
}
