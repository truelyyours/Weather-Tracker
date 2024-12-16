package com.example.weathertracker.composeutil

import android.graphics.drawable.Icon
import android.util.Log
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
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.example.network.ApiService
import com.example.network.RetrofitClient
import com.example.network.data.WeatherInfo
import com.example.weathertracker.R
import com.example.weathertracker.Utils
import com.example.weathertracker.ui.theme.BackgroundGray
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.CustomGray
import com.example.weathertracker.ui.theme.LightGray
import com.example.weathertracker.ui.theme.PoppinsFontFamily
import com.example.weathertracker.viewmodel.WeatherInfoLoadingState
import com.example.weathertracker.viewmodel.WeatherInfoViewModel
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LocationWeatherDetails(
    location: String,
    viewModel: WeatherInfoViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getWeather(location)
    }

    val stateFlow by viewModel.stateFlow.collectAsState() // Current State

        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {

            when (val tempState = stateFlow) {
                is WeatherInfoLoadingState.Error -> {
                    Text(text = tempState.message, minLines = 4, fontFamily = PoppinsFontFamily, style = TextStyle(fontSize = 40.sp), color = CustomBlack, textAlign = TextAlign.Center)
                }
                WeatherInfoLoadingState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(100.dp), color = CustomGray)
                }
                is WeatherInfoLoadingState.Success -> {
                    val imageUrl = tempState.weatherInfo.current.condition.icon.replace("64x64", "128x128")
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

                    LocationText(tempState.weatherInfo.location.name + " ")
                    Spacer(Modifier.height(24.dp))

                    Text(text = tempState.weatherInfo.current.temp_c.toString() + "\u02DA", fontFamily = PoppinsFontFamily, fontSize = 70.sp, fontWeight = FontWeight.Medium, color = CustomBlack,
                        textAlign = TextAlign.Center)

                    Spacer(Modifier.height(36.dp))

                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.height(75.dp).width(274.dp)
                            .clip(RoundedCornerShape(16.dp)).background(BackgroundGray)) {
                        ExtraInfoColumn("Humidity", tempState.weatherInfo.current.humidity.toString() + "%")
                        ExtraInfoColumn("UV", tempState.weatherInfo.current.uv.toString() + "%")
                        ExtraInfoColumn("Feels Like", tempState.weatherInfo.current.feelslike_c.toString() + "\u02DA")
                    }
                }
            }
        }
//    }
}

@Composable
fun LocationText(name: String) {
    val id = "navIcon"
    val textt = buildAnnotatedString {
        append(name)
        appendInlineContent(id, "[nav_icon]")
    }
    val inlineContent = mapOf(
        Pair(
            id,
            InlineTextContent(
                Placeholder(
                    width = 21.sp,
                    height = 21.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {

                Icon(painterResource(R.drawable.baseline_navigation_24),
                    contentDescription = null,
                    modifier = Modifier.rotate(45.0f))
            }
        )
    )

    Text(text = textt, inlineContent = inlineContent,
        fontFamily = PoppinsFontFamily, style = TextStyle(fontSize = 40.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        fontWeight = FontWeight.SemiBold, color = CustomBlack)

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