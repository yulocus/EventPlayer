package com.yulocus.eventplayer.api

import com.yulocus.eventplayer.bean.Alert
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    @GET(ApiConstants.API_GET_EVENT_LIST)
    fun getEventList(): Observable<MutableList<Alert>>
}