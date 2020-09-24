package com.example.testapp.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 文 件 名：RealtimeSponse
 * 描   述：TODO
 */
class Realtimesponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)
    data class Realtime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)
    data class AirQuality(val aqi: AQI)
    data class AQI(val chn: Float)
}