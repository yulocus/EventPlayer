package com.yulocus.eventplayer.api

import com.yulocus.eventplayer.bean.response.EventsResponse
import io.reactivex.Observable

object ApiManager {
    private val apiService by lazy { ApiServiceFactory.getRetrofit()!!.create(ApiService::class.java) }

    fun getEventList(headers: Map<String, String>, values: Map<String, String>): Observable<EventsResponse> =
            apiService.getEventList(headers, values)

}