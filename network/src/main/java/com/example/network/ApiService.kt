package com.example.network

import com.example.network.data.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String): WeatherInfo

//    @GET("/v1/search.json")
//    suspend fun getAutoCompleteResults(
//        @Query("key") key: String,
//        @Query("q") q:String
//    ):
}