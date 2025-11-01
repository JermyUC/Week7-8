package com.jermy.weatherapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jermy.weatherapp.BuildConfig
import com.jermy.weatherapp.ui.viewmodel.*
import com.jermy.weatherapp.ui.view.components.WeatherDisplay
import com.jermy.weatherapp.ui.view.components.SearchBar

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.jermy.weatherapp.R
@Composable
fun MainScreen(viewModel: WeatherViewModel) {
    val state by viewModel.state.collectAsState()
    var city by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {Box(modifier = Modifier.fillMaxSize()) {
        // 🖼 Background image (the one you just added)
        Image(
            painter = painterResource(id = R.drawable.bg_weather)
            ,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🌤 Your existing UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                city = city,
                onCityChange = { city = it },
                onSearch = {
                    viewModel.fetchWeather(city, BuildConfig.WEATHER_API_KEY)
                }
            )

            when (state) {
                is WeatherState.Empty -> CenterMessage("Search for a city")
                is WeatherState.Loading -> CenterMessage("Loading...")
                is WeatherState.Error -> CenterMessage((state as WeatherState.Error).message)
                is WeatherState.Success -> {
                    val data = (state as WeatherState.Success).data
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        WeatherDisplay(data)
                    }

                }
            }
        }
    }

    }
}

@Composable
fun CenterMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}
