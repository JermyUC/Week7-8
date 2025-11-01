package com.jermy.weatherapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jermy.weatherapp.ui.model.WeatherResponse
import com.jermy.weatherapp.ui.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Empty : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val state: StateFlow<WeatherState> = _state

    fun fetchWeather(city: String, apiKey: String) {
        if (city.isBlank()) {
            _state.value = WeatherState.Error("City cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                _state.value = WeatherState.Loading
                Log.d("WeatherApp", "Fetching weather for: $city")

                val response = repo.getWeather(city.trim(), apiKey)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.weather.isNullOrEmpty()) {
                        _state.value = WeatherState.Error("Weather data missing")
                    } else {
                        _state.value = WeatherState.Success(body)
                    }
                } else {
                    val msg = when (response.code()) {
                        401 -> "Invalid API key"
                        404 -> "City not found"
                        else -> "HTTP ${response.code()} ${response.message()}"
                    }
                    Log.e("WeatherApp", msg)
                    _state.value = WeatherState.Error(msg)
                }

            } catch (e: Exception) {
                Log.e("WeatherApp", "Exception: ${e.message}", e)
                _state.value = WeatherState.Error("Network error: ${e.localizedMessage}")
            }
        }
    }
}
