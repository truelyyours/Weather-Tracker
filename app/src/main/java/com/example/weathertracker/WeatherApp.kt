package com.example.weathertracker

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltAndroidApp
class WeatherApp: Application() {

    companion object {
        private lateinit var instance: WeatherApp
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather")
        private val KEY_NAME = stringPreferencesKey("city_name")

        fun getNonUiAppContext():Context {
//            Log.i("WeatherApp:: ", instance.applicationContext.getString(R.string.base_url))
            return instance.applicationContext
        }

        fun getStoredCityName(): Flow<String?> {
            return getNonUiAppContext().dataStore.data.map { pref ->
                pref[KEY_NAME] ?: ""
            }
        }

        suspend fun storeCityName(location: String) {
            getNonUiAppContext().dataStore.edit { pref ->
                pref[KEY_NAME] = location
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}