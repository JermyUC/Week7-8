package com.jermy.weatherapp.ui.model

data class WeatherResponse(
    val name: String,          // City name
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: Long,
    val sys: Sys?,
    val rain: Rain? = null
    )

data class Main(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int
)
data class Sys(
    val sunrise: Long,
    val sunset: Long
)
data class Rain(
    val `1h`: Double? = null
)


data class Wind(
    val speed: Double
)

data class Clouds(
    val all: Int
)


