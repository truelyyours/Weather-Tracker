package com.example.weathertracker.composeutil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.WeatherApp
import com.example.weathertracker.ui.theme.BackgroundGray
import com.example.weathertracker.ui.theme.CustomGray
import com.example.weathertracker.ui.theme.IconGray
import com.example.weathertracker.ui.theme.LightGray
import com.example.weathertracker.ui.theme.PoppinsFontFamily
import com.example.weathertracker.viewmodel.SearchBoxViewModel
import kotlinx.coroutines.launch


@Composable
fun SearchBox(searchBoxViewModel: SearchBoxViewModel = hiltViewModel()) {

    DisposableEffect(key1 = Unit) {
        val job = searchBoxViewModel.observeSearch()
        onDispose { job.cancel() }
    }

    Column(modifier = Modifier.fillMaxSize()
        .padding(top = 44.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        val screenState by searchBoxViewModel.uiState.collectAsStateWithLifecycle()

        Row(modifier = Modifier.fillMaxWidth()
            .height(46.dp)
            .background(color = BackgroundGray, shape = RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            var isHintDisplayed by remember {
                mutableStateOf(false)
            }
            val focusManager = LocalFocusManager.current

            BasicTextField(
                state = searchBoxViewModel.searchTextFieldState,
                textStyle = TextStyle(fontSize = 15.sp, fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                onKeyboardAction = KeyboardActionHandler {
                    focusManager.clearFocus()
                },
                modifier = Modifier.padding(start = 20.dp)
            )

            Icon(Icons.Rounded.Search, contentDescription = "Search Icon", tint = IconGray
            ,modifier = Modifier.padding(end = 14.5.dp).size(17.5.dp))
        }

        Spacer(Modifier.height(32.dp))
        when (val state = screenState) {
            SearchBoxViewModel.ScreenState.Empty -> { // Nothing to do
            }
            is SearchBoxViewModel.ScreenState.Error -> { // Nothing to do
            }
            is SearchBoxViewModel.ScreenState.Response -> {
                SearchResultCard(
                    state.weatherInfo.location.name,
                    state.weatherInfo.current.temp_c.toString() + "\u02DA",
                    state.weatherInfo.current.condition.icon,
                    onClick = {
                        searchBoxViewModel.viewModelScope.launch {
                            saveLocation(state.weatherInfo.location.name)
                        }
                    }
                )
            }
            SearchBoxViewModel.ScreenState.Searching -> {
                CircularProgressIndicator(modifier = Modifier.size(45.dp), color = CustomGray)
            }
        }
    }

}

suspend fun saveLocation(loc: String) {
    WeatherApp.storeCityName(loc)
}