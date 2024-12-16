package com.example.weathertracker.hiltdi

import com.example.network.RetrofitClient
import com.example.weathertracker.R
import com.example.weathertracker.WeatherApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(): RetrofitClient {
//        Single instance. So this has to be configured here only and it's a singleton.
        val retrofitClient = RetrofitClient()
        retrofitClient.createInstance(WeatherApp.getNonUiAppContext().getString(R.string.base_url))
        return retrofitClient
    }
}