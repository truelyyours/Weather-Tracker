package com.example.weathertracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.data.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
): ViewModel() {

    private val _internalState = MutableStateFlow<WeatherInfoLoadingState>(
        value = WeatherInfoLoadingState.Loading
    )
    val stateFlow = _internalState.asStateFlow() // External can read only from this

    private var cachedInfo: WeatherInfo? = null

    fun getWeather(location: String) = viewModelScope.launch {
        if (cachedInfo != null && cachedInfo!!.location.name == location) {
//            _internalState.update {  }
            return@launch
        }
        _internalState.update {
            return@update WeatherInfoLoadingState.Loading
        }
        currentWeatherRepository.fetchWeather(location).onSuccess { info ->
            _internalState.update {
                cachedInfo = info
                return@update WeatherInfoLoadingState.Success(
                    weatherInfo = info
                )
            }

        }.onGenericError { code, errorMessage ->
            Log.e("LocationWeatherDetails::", "Generic Error $code && $errorMessage")
            _internalState.update {
                return@update WeatherInfoLoadingState.Error(
                    message = errorMessage ?: "Unknown Error"
                )
            }
        }.onFailure { exception ->
            Log.e("LocationWeatherDetails:: ", exception.message.toString())
            _internalState.update {
                return@update WeatherInfoLoadingState.Error(
                    message = "No Network Connection"
                )
            }
        }
    }
}

sealed interface WeatherInfoLoadingState {
    data object Loading: WeatherInfoLoadingState
    data class Error(val message: String): WeatherInfoLoadingState
    data class Success(val weatherInfo: WeatherInfo): WeatherInfoLoadingState
}