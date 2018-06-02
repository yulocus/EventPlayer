package com.yulocus.eventplayer.bean

import com.google.gson.annotations.SerializedName

data class Param (
        @SerializedName("temperature") var temperature: String = "",
        @SerializedName("setting") var setting: String = ""
)