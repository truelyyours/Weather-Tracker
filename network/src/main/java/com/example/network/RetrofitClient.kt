package com.example.network

import android.util.Log
import com.example.network.data.WeatherInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private lateinit var BASE_URL: String
//    lateinit var client: RetrofitClient
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
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
        apiService = getApiClient()
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

    suspend fun getCurrentWeather(dispatcher: CoroutineDispatcher, key: String, location: String): GracefulApiCall<WeatherInfo> {
        return safeApiCall(dispatcher) {
            withContext(dispatcher) {
                apiService.getCurrentWeather(key, location)
            }
        }
    }

    private suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): GracefulApiCall<T> {
        return withContext(dispatcher) {
            try {
                GracefulApiCall.Success(data = apiCall.invoke())
            } catch (e: Exception) {
                when (e) {
                    is IOException -> GracefulApiCall.Failure(error = e)
                    is HttpException -> {
                        val code = e.code()
                        val errorResponse = parseErrorBody(e)

                        Log.i("Retrofit Client::", "" + errorResponse)
                        GracefulApiCall.GenericError(code, errorResponse)
                    }
                    else -> {
                        GracefulApiCall.Failure(error = e)
                    }
                }
//            GracefulApiCall.Failure(error = e)
            }
        }
    }
}

private fun parseErrorBody(exp: HttpException): String {
    try {
        val errorJson = exp.response()?.errorBody()?.string()?.trim()
            ?.let { JSONObject(it) }

        val message = errorJson?.getJSONObject("error")?.getString("message") ?: "Error Related to API key."
        val code = errorJson?.getJSONObject("error")?.getString("code") ?: "N/A"

        return "Error Code: $code - $message"
    } catch (e: Exception) {
        return ""
    }
}

sealed interface GracefulApiCall<T> {
    // It will be either a Success or a failure
    data class Success<T>(val data: T): GracefulApiCall<T>
    data class GenericError<T>(val code: Int? = null, val error: String? = null): GracefulApiCall<T>
    data class Failure<T>(val error: Exception): GracefulApiCall<T>

    suspend fun onSuccess(block: suspend (T) -> Unit): GracefulApiCall<T> {
        // Acts as callback which is executed on Success
        if (this is Success) block(data)
        return this
    }

    suspend fun onFailure(block: (Exception) -> Unit): GracefulApiCall<T> {
        // Acts as callback which is executed on Success
        if (this is Failure) block(error)
        return this
    }

    suspend fun onGenericError(block: (Int?, String?) -> Unit): GracefulApiCall<T> {
        if (this is GenericError) block(code, error)
        return this
    }
}