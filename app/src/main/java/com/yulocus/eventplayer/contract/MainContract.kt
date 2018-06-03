package com.yulocus.eventplayer.contract

import com.yulocus.eventplayer.base.MvpView
import com.yulocus.eventplayer.bean.Alert

interface MainContract {
    /**
     * View interface
     */
    interface View : MvpView {
        fun showEvents(list: MutableList<Alert>)
    }

    /**
     * Presenter interface
     */
    interface Presenter {
        fun loadEvents()
    }
}