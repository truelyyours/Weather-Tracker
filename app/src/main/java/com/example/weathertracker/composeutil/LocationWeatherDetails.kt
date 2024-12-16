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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LocationWeatherDetails() {

    var weatherInfo by remember {
        mutableStateOf<WeatherInfo?> (null)
    }
    var networkError by remember {
        mutableStateOf(false)
    }
    var locationError by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        RetrofitClient.getCurrentWeather(Dispatchers.IO, Utils.getWeatherAppApiKey(), "S").onSuccess {
            weatherInfo = it
        }.onGenericError{code, error ->
//            This means location not found.
            networkError = false
            locationError = true
            Log.e("LocationWeatherDetails::", "Generic Error $code && $error")
        }.onFailure {
//            TODO: Show empty. DO nothing This mean NO INTERNET
            networkError = true
            locationError = false
            Log.e("LocationWeatherDetails:: ", it.message.toString())
        }
    }

    if (networkError) {
        Text(text = "No Internet Connection!", minLines = 4, fontFamily = PoppinsFontFamily, style = TextStyle(fontSize = 40.sp), color = CustomBlack, textAlign = TextAlign.Center)
    } else if (locationError) {
        Text(text = "No location found with given name!", minLines = 4, fontFamily = PoppinsFontFamily, style = TextStyle(fontSize = 40.sp), color = CustomBlack, textAlign = TextAlign.Center)
    } else if (weatherInfo == null) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp), color = CustomGray)
    } else {
        //    The figma designs are not centre aligned but just "looks" center aligned.
        //    I am center aligning as its easier and convenient.
        networkError = false
        locationError = false
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {

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

            LocationText(weatherInfo!!.location.name + " ")
            Spacer(Modifier.height(24.dp))

            Text(text = weatherInfo!!.current.temp_c.toString() + "\u02DA", fontFamily = PoppinsFontFamily, fontSize = 70.sp, fontWeight = FontWeight.Medium, color = CustomBlack,
                textAlign = TextAlign.Center)

            Spacer(Modifier.height(36.dp))

            Row (verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.height(75.dp).width(274.dp)
                    .clip(RoundedCornerShape(16.dp)).background(BackgroundGray)) {
                ExtraInfoColumn("Humidity", weatherInfo!!.current.humidity.toString() + "%")
                ExtraInfoColumn("UV", weatherInfo!!.current.uv.toString() + "%")
                ExtraInfoColumn("Feels Like", weatherInfo!!.current.feelslike_c.toString() + "\u02DA")
            }
        }
    }
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
        fontFamily = PoppinsFontFamily, fontSize = 30.sp, fontWeight = FontWeight.SemiBold, color = CustomBlack)

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