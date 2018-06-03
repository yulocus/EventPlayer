package com.yulocus.eventplayer.bean

import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Alert (
        @SerializedName("alert_id") var alertId: String = "",
        @SerializedName("device_id") var deviceId: String = "",
        @SerializedName("image") var image: String = "",
        @SerializedName("video") var video: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("tp") var tp: String = "",
        @SerializedName("ts") var ts: Long = 0L,
        @SerializedName("user") var user: String = ""
): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelAlert.CREATOR
    }
}