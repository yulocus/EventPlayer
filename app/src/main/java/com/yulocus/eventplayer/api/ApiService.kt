package com.yulocus.eventplayer.api

import com.yulocus.eventplayer.bean.response.EventsResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface ApiService {
    @GET(ApiConstants.API_GET_EVENT_LIST)
    fun getEventList(@HeaderMap headers: Map<String, String>, @Body values: Map<String, String>): Observable<EventsResponse>
}