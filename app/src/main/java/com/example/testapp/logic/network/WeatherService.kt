package com.example.testapp.logic.network

import com.example.testapp.MyApplication
import com.example.testapp.logic.model.DailyResponse
import com.example.testapp.logic.model.Realtimesponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 文 件 名：WeatherService
 * 描   述：天气接口
 */
interface WeatherService {
    @GET("v2.5/${MyApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<Realtimesponse>

    @GET("v2.5/${MyApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}