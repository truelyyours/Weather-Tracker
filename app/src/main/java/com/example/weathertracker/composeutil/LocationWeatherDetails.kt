package com.example.weathertracker.composeutil

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.RequestState
import com.bumptech.glide.integration.compose.placeholder
import com.example.network.ApiService
import com.example.network.data.WeatherInfo
import com.example.weathertracker.Utils
import com.example.weathertracker.ui.theme.BackgroundGray
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.CustomGray
import com.example.weathertracker.ui.theme.LightGray
import com.example.weathertracker.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LocationWeatherDetails(apiService: ApiService) {

    var weatherInfo by remember {
        mutableStateOf<WeatherInfo?> (null)
    }

    LaunchedEffect(Unit) {
        weatherInfo = apiService.getCurrentWeather(Utils.getWeatherAppApiKey(), "State College")
    }

    if (weatherInfo == null) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp), color = Color.Cyan)
    } else {
        //    The figma designs are not centre aligned but just "looks" center aligned.
        //    I am center aligning as its easier and convenient.
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally){

            val imageUrl = weatherInfo!!.current.condition.icon.replace("64x64", "128x128")
            GlideSubcomposition(modifier = Modifier.size(123.dp),
                model = "https:$imageUrl"
            ) {
                when (state) {
                    RequestState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.size(60.dp), color = Color.Cyan
                    )
                    RequestState.Failure -> Icon(Icons.Default.Warning,
                        tint = Color.Red, contentDescription = null)
                    is RequestState.Success -> Image(painter = painter,
                        modifier = Modifier.size(123.dp),
                        contentDescription = "Weather Icon Image")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(text = weatherInfo!!.location.name, fontFamily = PoppinsFontFamily, fontSize = 30.sp, fontWeight = FontWeight.SemiBold, color = CustomBlack)

            Spacer(Modifier.height(24.dp))

            Text(text = weatherInfo!!.current.temp_c.toString() + "\u00B0", fontFamily = PoppinsFontFamily, fontSize = 70.sp, fontWeight = FontWeight.Medium, color = CustomBlack)

            Spacer(Modifier.height(36.dp))

            Row (verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.height(75.dp).width(274.dp)
                    .clip(RoundedCornerShape(16.dp)).background(BackgroundGray)) {
                ExtraInfoColumn("Humidity", weatherInfo!!.current.humidity.toString() + "%")
                ExtraInfoColumn("UV", weatherInfo!!.current.uv.toString() + "%")
                ExtraInfoColumn("Feels Like", weatherInfo!!.current.feelslike_c.toString() + "°")
            }

        }
    }
}

@Composable
fun ExtraInfoColumn(title: String, info: String) {
//    TODO: Size of feels like title is 8dp
    Column {
        Text(text = title, fontSize = 12.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, color = LightGray)
        Spacer(Modifier.height(4.dp))
        Text(text = info, fontSize = 15.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, color = CustomGray)

    }
}