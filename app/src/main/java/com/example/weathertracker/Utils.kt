package com.example.weathertracker

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

object Utils {

    private var WEATHER_APP_API_KEY = ""

    @Composable
    fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

    fun getWeatherAppApiKey(): String {
        Log.i("Utils::", BuildConfig.WEATHER_APP_API_KEY)
        if (WEATHER_APP_API_KEY.isNotEmpty())
            return WEATHER_APP_API_KEY
        else {
            WEATHER_APP_API_KEY = BuildConfig.WEATHER_APP_API_KEY
            return WEATHER_APP_API_KEY
        }
    }
}