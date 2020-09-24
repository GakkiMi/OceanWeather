package com.example.testapp.logic.dao

import android.content.Context
import com.example.testapp.MyApplication
import com.example.testapp.logic.model.Place
import com.google.gson.Gson
import androidx.core.content.edit

/**
 * 文 件 名：PlaceDao
 * 描   述：存储sharedpreference
 */
object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit() {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlacedSaved() = sharedPreferences().contains("place")
    private fun sharedPreferences() = MyApplication.context.getSharedPreferences("ocean_weather", Context.MODE_PRIVATE)

}