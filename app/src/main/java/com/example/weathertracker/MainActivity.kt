package com.example.weathertracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.network.RetrofitClient
import com.example.weathertracker.composeutil.LocationWeatherDetails
import com.example.weathertracker.composeutil.NoCitySelected
import com.example.weathertracker.composeutil.SearchBox
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.PoppinsFontFamily
import com.example.weathertracker.ui.theme.WeatherTrackerTheme
import com.example.weathertracker.viewmodel.SearchBoxViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    Init Retrofit Client
    @Inject
    lateinit var retrofitClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTrackerTheme {

                val searchBoxViewModel: SearchBoxViewModel = hiltViewModel()
                val focusManager = LocalFocusManager.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).fillMaxSize().pointerInput(Unit) {
                        detectTapGestures {
                            focusManager.clearFocus()
                            searchBoxViewModel.exitSearch()
                        }
                    },
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(44.dp))
                        SearchBox(searchBoxViewModel)
                        if (Utils.getWeatherAppApiKey().isEmpty()) {
                            Text(text = "No Weather API key supplied in Utils or Environment variable", fontFamily = PoppinsFontFamily, style = TextStyle(fontSize = 40.sp), color = CustomBlack, textAlign = TextAlign.Center)
                        return@Column
                        }
                        Log.i("MainActivity::", "SearboxModelUIState: " + (searchBoxViewModel.uiState.collectAsState().value == SearchBoxViewModel.ScreenState.Empty))
                        if (searchBoxViewModel.uiState.collectAsState().value == SearchBoxViewModel.ScreenState.Empty) {
                            LocalFocusManager.current.clearFocus()
                            val city = WeatherApp.getStoredCityName().collectAsState("").value
                            if (city.isNullOrEmpty()) {
                                Spacer(Modifier.height(240.dp))
                                NoCitySelected()
                            } else {
                                Spacer(Modifier.height(40.dp))
                                LocationWeatherDetails(city)
                            }
                        }
                    }
                }
            }
        }
    }
}