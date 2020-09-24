package com.example.testapp.logic

import androidx.lifecycle.liveData
import com.example.testapp.logic.dao.PlaceDao
import com.example.testapp.logic.model.Place
import com.example.testapp.logic.model.Weather
import com.example.testapp.logic.network.OceanWeatherNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

import java.lang.Exception
import kotlin.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 文 件 名：Repository
 * 描   述：仓库层的统一封装入口
 */
object Repository {

    /**
     * liveData是ktx库提供的一个功能 可以自动构建并返回liveData对象
     * 可以在代码块提供一个挂起函数的上下文 并将线程设置成io 所有的代码块都在子线程中了
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = OceanWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            //请求成功，读取数据并返回
            val places = placeResponse.places
            Result.success(places)
        } else {
            //请求失败，数据为空 把错误信息包装抛上层
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                OceanWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                OceanWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(RuntimeException("realtime response status is ${realtimeResponse.status}" + "daily response status is ${dailyResponse.status}"))
            }

        }
    }


    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
            liveData<Result<T>>(context) {
                var result = try {
                    block()
                } catch (e: Exception) {
                    Result.failure<T>(e)
                }
                emit(result)
            }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlacedSaved()

}