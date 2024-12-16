package com.example.network.data


data class WeatherInfo(
    val location: LocationInfo,
    val current: CurrentWeatherInfo
) {
    data class LocationInfo(
        val name: String,
        val region: String,
        val country: String
    )

    data class CurrentWeatherInfo(
        val last_updated: String,
        val temp_c: Float,
        val humidity: Int,
        val feelslike_c: Float,
        val uv: Float,
        val condition: ConditionInfo
    )

    data class ConditionInfo(
        val text: String,
        val icon: String
    )
}
