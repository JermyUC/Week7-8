package com.jermy.weatherapp.ui.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jermy.weatherapp.R
import com.jermy.weatherapp.ui.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

val PlaceholderPrimaryColor = Color.White
val PlaceholderSecondaryColor = Color.White.copy(alpha = 0.7f)
val PlaceholderCardColor = Color.White.copy(alpha = 0.15f)

fun formatTimeOnly(timestamp: Long?): String {
    if (timestamp == null) return "-"
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}

fun formatDate(timestamp: Long?): String {
    if (timestamp == null) return "-"
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return format.format(date)
}

@Composable
fun WeatherDisplay(data: WeatherResponse) {
    // ✅ Make LazyColumn the root
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                WeatherContent(data)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// MAIN CONTENT
// ---------------------------------------------------------------------------
@Composable
fun WeatherContent(data: WeatherResponse) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // HEADER
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Pin",
                tint = PlaceholderPrimaryColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = data.name ?: "-",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = PlaceholderPrimaryColor
            )
        }

        Text(
            text = formatDate(data.dt),
            fontSize = 16.sp,
            color = PlaceholderSecondaryColor
        )

        // MAIN WEATHER + PANDA
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                AsyncWeatherIcon(iconCode = data.weather.firstOrNull()?.icon)
                Text(
                    text = data.weather.firstOrNull()?.main ?: "-",
                    color = PlaceholderPrimaryColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${data.main.temp}°C",
                    color = PlaceholderPrimaryColor,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            val pandaRes = when (data.weather.firstOrNull()?.main?.lowercase()) {
                "clear" -> R.drawable.blue_and_black_bold_typography_quote_poster
                "rain" -> R.drawable.blue_and_black_bold_typography_quote_poster_2
                "clouds" -> R.drawable.blue_and_black_bold_typography_quote_poster_3
                else -> R.drawable.download
            }

            Image(
                painter = painterResource(id = pandaRes),
                contentDescription = "Panda",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterEnd)
            )
        }

        // DETAIL CARDS
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailCard("HUMIDITY", "${data.main.humidity}%", R.drawable.icon_humidity, Modifier.weight(1f))
                DetailCard("WIND", "${data.wind?.speed ?: 0.0} m/s", R.drawable.icon_wind, Modifier.weight(1f))
                DetailCard("FEELS LIKE", "${data.main.feels_like}°", R.drawable.icon_feels_like, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailCard("RAIN", "${data.rain?.`1h` ?: 0.0} mm", R.drawable.vector_2, Modifier.weight(1f))
                DetailCard("PRESSURE", "${data.main.pressure} hPa", R.drawable.vector, Modifier.weight(1f))
                DetailCard("CLOUDS", "${data.clouds?.all ?: 0}%", R.drawable.cloud, Modifier.weight(1f))
            }
        }

        // SUNRISE / SUNSET
        Card(
            backgroundColor = Color.Transparent,
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeCard("SUNRISE", formatTimeOnly(data.sys?.sunrise), R.drawable.vector)
                TimeCard("SUNSET", formatTimeOnly(data.sys?.sunset), R.drawable.vector_21png)
            }
        }
    }
}


@Composable
fun DetailCard(title: String, value: String, iconRes: Int, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = PlaceholderCardColor,
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        modifier = modifier.height(110.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(PlaceholderPrimaryColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PlaceholderPrimaryColor
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = PlaceholderSecondaryColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TimeCard(label: String, time: String, iconRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(PlaceholderPrimaryColor)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = PlaceholderSecondaryColor)
        Text(text = time, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlaceholderPrimaryColor)
    }
}

@Composable
fun AsyncWeatherIcon(iconCode: String?) {
    if (iconCode != null) {
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        AsyncImage(
            model = iconUrl,
            contentDescription = "Weather icon",
            modifier = Modifier.size(64.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = "Placeholder icon",
            modifier = Modifier.size(64.dp),
            colorFilter = ColorFilter.tint(PlaceholderPrimaryColor)
        )
    }
}
