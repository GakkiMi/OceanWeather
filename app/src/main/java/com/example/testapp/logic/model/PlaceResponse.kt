package com.example.testapp.logic.model

/**
 * 文 件 名：PlaceResponse
 * 描   述：TODO
 */
data class PlaceResponse(val status:String,val places:List<Place>)
data class Place(val name:String,val location:Location,val address:String)
data class Location(val lng:String,val lat:String)