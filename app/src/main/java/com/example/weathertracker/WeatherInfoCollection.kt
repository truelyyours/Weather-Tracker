package com.example.weathertracker

import javax.inject.Inject

// WeatherInfoCollection, WeatherState can be in a different class but,
// for small scale its fine to keep theme here. Not used anywhere else

class WeatherInfoCollection @Inject constructor(
//    Add the network client that fetches all the data
) {

}

sealed interface WeatherState {
    data object Loading: WeatherState
    data class Error(val message: String): WeatherState
//    data class Success(
////        will have the Weather Data class that stores all the data for given city
//    )
}