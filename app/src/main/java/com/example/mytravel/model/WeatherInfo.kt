package com.example.mytravel.model

data class WeatherInfo(
    val date: String,
    val state: String,
    val min_temp: Double,
    val max_temp: Double,
    val temp: Int,
    val wind_speed: Double,
    val humidity: Int,
    val weather_state_abbr :String
)
