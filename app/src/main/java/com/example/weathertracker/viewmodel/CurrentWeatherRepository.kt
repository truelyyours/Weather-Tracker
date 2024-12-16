package com.example.weathertracker.viewmodel

import com.example.network.GracefulApiCall
import com.example.network.RetrofitClient
import com.example.network.data.WeatherInfo
import com.example.weathertracker.Utils
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(private val retrofitClient: RetrofitClient) {
    suspend fun fetchWeather(location: String): GracefulApiCall<WeatherInfo> {
        return retrofitClient.getCurrentWeather(Dispatchers.IO, Utils.getWeatherAppApiKey(), location)
    }
}