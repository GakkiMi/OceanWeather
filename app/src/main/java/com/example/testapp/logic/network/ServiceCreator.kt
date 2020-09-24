package com.example.testapp.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 文 件 名：ServiceCreator
 * 描   述：TODO
 */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    fun <T> create(serviceClass: Class<T>) :T = retrofit.create(serviceClass)
    inline fun <reified T> create():T = create(T::class.java)
}