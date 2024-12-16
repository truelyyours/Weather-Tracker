package com.example.weathertracker

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.RetrofitClient
import com.example.network.data.WeatherInfo
import com.example.weathertracker.ui.theme.BackgroundGray
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.LightGray
import com.example.weathertracker.ui.theme.PoppinsFontFamily
import com.example.weathertracker.ui.theme.WeatherTrackerTheme



lateinit var appCtx: Context

class MainActivity : ComponentActivity() {

//    Init Retrofit Client
    init {
        RetrofitClient.createInstance(WeatherApp.getNonUiAppContext().getString(R.string.base_url))
    }

    private val apiClient = RetrofitClient.getApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        appCtx = applicationContext
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTrackerTheme {
                var weatherInfo by remember {
                    mutableStateOf<WeatherInfo?> (null)
                }

                LaunchedEffect(Unit) {
                    weatherInfo = apiClient.getCurrentWeather(Utils.getWeatherAppApiKey(), "State College")
                }

//                val viewModel = DataViewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(44.dp))
                        SearchCity()
                        Spacer(Modifier.height(240.dp))
                        NoCitySelected()
                        Spacer(Modifier.height(40.dp))
                        weatherInfo?.location?.let { Text(text = it.name, fontFamily = PoppinsFontFamily, fontSize = 25.sp) }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCity() {

    val searchQuery = remember { mutableStateOf("") }
    val isSearching = remember { mutableStateOf(false) }
    SearchBar(modifier = Modifier.padding(horizontal = 24.dp).clip(RoundedCornerShape(16.dp)),
        inputField = {
            SearchBarDefaults.InputField(
//                modifier = Modifier.height(46.dp),
                query = searchQuery.value,
                onSearch = ::tempOnTextChange,
                onQueryChange = ::tempOnTextChange,
                expanded = isSearching.value,
                onExpandedChange = { isSearching.value = !isSearching.value },
                placeholder = { Text("Search Location", fontFamily = PoppinsFontFamily,
                    fontSize = 15.sp, color = LightGray, fontWeight = FontWeight.W400, lineHeight = 22.5.sp)},
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null,
                    tint = LightGray) },
            )
        },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        expanded = isSearching.value,
        onExpandedChange = {isSearching.value = !isSearching.value}) {

    }
}

@Composable
fun NoCitySelected() {
    Column(modifier = Modifier.padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = appCtx.getString(R.string.no_city_selected),
            fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp, lineHeight = 45.sp, textAlign = TextAlign.Center,
            maxLines = 1, color = CustomBlack, modifier = Modifier.height(50.dp)
       )
        Text(text = appCtx.getString(R.string.please_search_city),
            fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp, lineHeight = 22.5.sp, textAlign = TextAlign.Center,
            maxLines = 1, color = CustomBlack
        )
    }
}

@Composable
fun LocationDetails() {
//    The figma designs are not centre aligned but just "looks" center aligned.
//    I am center aligning as its easier and convenient.
    Column(modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally){

        Column(horizontalAlignment = Alignment.CenterHorizontally) {


        }

    }
}

fun tempOnTextChange(text: String): String {
    return "hello __ $text"
}