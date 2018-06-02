package com.yulocus.eventplayer.bean.response

import com.google.gson.annotations.SerializedName
import com.yulocus.eventplayer.api.ApiConstants

open class BaseResponse<out T> {
    @SerializedName("meta")
    val meta: Meta? = null

    @SerializedName("result")
    val result: T? = null

    val isSuccessful : Boolean
        get() = ApiConstants.RESPONSE_CODE_SUCCESS == meta?.status
}
