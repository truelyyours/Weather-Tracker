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
        if (WEATHER_APP_API_KEY.isNotEmpty())
            return WEATHER_APP_API_KEY
        else {
            try {
                WEATHER_APP_API_KEY = System.getenv("WEATHER_APP_API_KEY")?.toString() ?: ""
                Log.i("Utils::", "The api key is $WEATHER_APP_API_KEY")
                if (WEATHER_APP_API_KEY.isEmpty())
                    throw NullPointerException("No Weather App API key found.")
            } catch (npe: NullPointerException) {
                Log.i("Utils::", "getWeatherAppApiKey: " + npe.message)
            } catch (e: Exception) {
                Log.i("Utils::", "getWeatherAppApiKey: An exception occured __ " + e.message)
                e.printStackTrace()
            }
            return WEATHER_APP_API_KEY
        }
    }
}