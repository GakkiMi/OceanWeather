package com.example.testapp.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.testapp.logic.model.Place

/**
 * 文 件 名：PlaceViewModel
 * 描   述：TODO
 */
class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData=Transformations.switchMap(searchLiveData){
        query-> Repository.searchPlaces(query)
    }

}