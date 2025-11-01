package com.jermy.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.jermy.weatherapp.ui.repository.WeatherRepository
import com.jermy.weatherapp.ui.service.WeatherService
import com.jermy.weatherapp.ui.viewmodel.WeatherViewModel
import com.jermy.weatherapp.ui.view.MainScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🌐 Placeholder: replace with real API base URL
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/") // <-- replace if using another API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherService::class.java)
        val repo = WeatherRepository(api)
        val viewModel = WeatherViewModel(repo)

        setContent {
            MainScreen(viewModel)
        }
    }
}
