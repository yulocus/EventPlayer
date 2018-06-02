package com.yulocus.eventplayer.bean.response

import com.google.gson.annotations.SerializedName
import com.yulocus.eventplayer.bean.Alert

class EventsResponse: BaseResponse<EventsResponse>() {
    data class Result (@SerializedName("alert") val alert: Alert)
}