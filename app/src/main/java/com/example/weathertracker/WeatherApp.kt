package com.example.weathertracker

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application() {

    companion object {
        private lateinit var instance: WeatherApp

        fun getNonUiAppContext():Context {
//            Log.i("WeatherApp:: ", instance.applicationContext.getString(R.string.base_url))
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}