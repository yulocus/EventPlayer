package com.yulocus.eventplayer.api

import com.yulocus.eventplayer.bean.Alert
import io.reactivex.Observable

object ApiManager {
    private val apiService by lazy { ApiServiceFactory.getRetrofit()!!.create(ApiService::class.java) }

    fun getEventList(): Observable<MutableList<Alert>> = apiService.getEventList()

}