package com.example.network

import com.example.network.data.WeatherInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private lateinit var BASE_URL: String
//    lateinit var client: RetrofitClient
    private lateinit var retrofit: Retrofit
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
//    Technically you should not have more than one client in project.
//    So this should be called once w/ BASE_URL
    fun createInstance(base_url: String) {
        BASE_URL = base_url
        retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    I think this may not be needed.
    fun getRetrofitClient(): Retrofit {
        return retrofit
    }

    fun getApiClient(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    fun getBaseUrl(): String {
        return BASE_URL
    }

}