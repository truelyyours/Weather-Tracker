package com.example.weathertracker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.map

@HiltAndroidApp
class WeatherApp: Application() {

    companion object {
        private lateinit var instance: WeatherApp
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather")

        fun getNonUiAppContext():Context {
//            Log.i("WeatherApp:: ", instance.applicationContext.getString(R.string.base_url))
            return instance.applicationContext
        }

        fun getStoredCityName():String {
             var city = ""
            val key_name = stringPreferencesKey("city_name")
            getNonUiAppContext().dataStore.data.map { pref ->
                city = pref[key_name] ?: ""
            }
            return "Surat"//city
        }

        suspend fun storeCityName(location: String) {
            getNonUiAppContext().dataStore.edit { pref ->
                val key_name = stringPreferencesKey("city_name")
                pref[key_name] = location
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}