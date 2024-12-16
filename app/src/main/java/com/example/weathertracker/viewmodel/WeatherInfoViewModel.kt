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



    fun getWeather(location: String) = viewModelScope.launch {
        _internalState.update {
            return@update WeatherInfoLoadingState.Loading
        }
        currentWeatherRepository.fetchWeather(location).onSuccess { info ->
            _internalState.update {
                return@update WeatherInfoLoadingState.Success(
                    weatherInfo = info
                )
            }

        }.onGenericError { code, errorMessage ->
            Log.e("LocationWeatherDetails::", "Generic Error $code && $errorMessage")
            _internalState.update {
                return@update WeatherInfoLoadingState.Error(
                    message = errorMessage + code.toString()
                )
            }
        }.onFailure { exception ->
            Log.e("LocationWeatherDetails:: ", exception.message.toString())
            _internalState.update {
                return@update WeatherInfoLoadingState.Error(
                    message = exception.message ?: "Unknown error"
                )
            }
        }
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow() // only readable by outside users

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
//        TODO: network call to fetch the region
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value)
            _searchText.value = ""
    }

}

sealed interface WeatherInfoLoadingState {
    object Loading: WeatherInfoLoadingState
    data class Error(val message: String): WeatherInfoLoadingState
    data class Success(val weatherInfo: WeatherInfo): WeatherInfoLoadingState
}