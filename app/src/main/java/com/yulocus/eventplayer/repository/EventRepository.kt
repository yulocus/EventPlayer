package com.yulocus.eventplayer.repository

import com.yulocus.eventplayer.api.ApiManager
import com.yulocus.eventplayer.bean.Alert
import io.reactivex.Observable
import timber.log.Timber

interface EventRepositoryImpl {
    fun loadEvents(): Observable<MutableList<Alert>>
}

class EventRepository: EventRepositoryImpl {
    override fun loadEvents(): Observable<MutableList<Alert>> {
        Timber.d("loadEvents()")
        return ApiManager.getEventList()
    }
}