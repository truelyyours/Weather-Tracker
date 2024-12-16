package com.example.weathertracker.composeutil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathertracker.R
import com.example.weathertracker.WeatherApp
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.PoppinsFontFamily

@Composable
fun NoCitySelected() {
    Column(modifier = Modifier.padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = WeatherApp.getNonUiAppContext().getString(R.string.no_city_selected),
            fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp, lineHeight = 45.sp, textAlign = TextAlign.Center,
            maxLines = 1, color = CustomBlack, modifier = Modifier.height(50.dp)
        )
        Text(text = WeatherApp.getNonUiAppContext().getString(R.string.please_search_city),
            fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp, lineHeight = 22.5.sp, textAlign = TextAlign.Center,
            maxLines = 1, color = CustomBlack
        )
    }
}