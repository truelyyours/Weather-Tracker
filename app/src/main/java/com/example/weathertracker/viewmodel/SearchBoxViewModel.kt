package com.example.weathertracker.viewmodel

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.data.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBoxViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
): ViewModel() {

    val searchTextFieldState = TextFieldState()

    sealed interface SearchState {
        data object Empty: SearchState
        data class UserQuery(val query: String): SearchState
    }

    sealed interface ScreenState {
        data object Empty: ScreenState
        data object Searching: ScreenState
        data class Error(val message: String): ScreenState
        data class Response(val weatherInfo: WeatherInfo): ScreenState
    }

    private val _uiState = MutableStateFlow<ScreenState> (ScreenState.Empty)
    val uiState = _uiState.asStateFlow()



    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchTextState: StateFlow<SearchState> = snapshotFlow { searchTextFieldState.text }
        .debounce(1200) // call below things if the user does not type for 1.2s
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.UserQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 3000),
            initialValue = SearchState.Empty
        )

    fun observeSearch() = viewModelScope.launch {
        searchTextState.collectLatest { searchState ->
            when (searchState) {
                is SearchState.Empty -> {
                    _uiState.update { ScreenState.Empty }
                }
                is SearchState.UserQuery -> searchWeatherInfo(searchState.query)
            }
        }
    }

    // Doing it this way so that in case the sear API is used it is easier to implement/extend
    private fun searchWeatherInfo(query: String) = viewModelScope.launch {
        _uiState.update { ScreenState.Searching }
        currentWeatherRepository.fetchWeather(query).onSuccess { info ->
            _uiState.update {
                ScreenState.Response(info)
            }
        }.onGenericError { code, errorMessage ->
            Log.e("SearchBoxViewModel::", "GenericError: $code && $errorMessage")
            _uiState.update { ScreenState.Error(errorMessage ?: "Unknown error happened $code") }
        }.onFailure { exp ->
            Log.e("SearchBoxViewModel::", exp.printStackTrace().toString())
            _uiState.update { ScreenState.Error(exp.message ?: "Unknown Failure Happened") }
        }
    }
}