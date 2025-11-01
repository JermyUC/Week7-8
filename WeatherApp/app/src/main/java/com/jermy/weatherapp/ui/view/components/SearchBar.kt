package com.jermy.weatherapp.ui.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(city: String, onCityChange: (String) -> Unit, onSearch: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            placeholder = { Text("Enter city name...") },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onSearch) { Text("Search") }
    }
}
