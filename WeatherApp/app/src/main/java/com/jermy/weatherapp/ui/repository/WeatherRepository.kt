package com.jermy.weatherapp.ui.repository

import com.jermy.weatherapp.ui.model.WeatherResponse
import com.jermy.weatherapp.ui.service.WeatherService
import retrofit2.Response

class WeatherRepository(private val api: WeatherService) {
    suspend fun getWeather(city: String, apiKey: String): Response<WeatherResponse> {
        return api.getWeatherByCity(city, apiKey)
    }
}
