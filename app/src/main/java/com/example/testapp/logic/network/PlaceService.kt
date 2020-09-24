package com.example.testapp.logic.network

import com.example.testapp.MyApplication
import com.example.testapp.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 文 件 名：PlaceService
 * 描   述：TODO
 */
interface PlaceService {

    @GET("v2/place?token=${MyApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query:String): Call<PlaceResponse>

}