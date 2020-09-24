package com.example.testapp.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 文 件 名：OceanWeatherNetwork
 * 描   述：网络工具类
 */
object OceanWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    /**
     * 搜索最近几天天气的api
     */
    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng, lat).await()

    /**
     * 搜索实时天气的api
     */
    suspend fun getRealtimeWeather(lng: String, lat: String) = weatherService.getRealtimeWeather(lng, lat).await()

    /**
     * 搜索全球地区的api接口 调用的自己定义的await函数来简化写法
     */
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    /**
     * suspend,协程 这里对所有call<T>定义了一个扩展方法await 里面对数据进行了处理和封装
     * 使用携程的写法俩简化Retrofit回调
     */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        //请求成功，继续
                        continuation.resume(body)
                    } else {
                        //请求成功但无结果 继续
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}