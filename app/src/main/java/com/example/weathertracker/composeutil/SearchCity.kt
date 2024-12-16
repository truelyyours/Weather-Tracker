package com.example.weathertracker.composeutil

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathertracker.tempOnTextChange
import com.example.weathertracker.ui.theme.LightGray
import com.example.weathertracker.ui.theme.PoppinsFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCity() {

    val searchQuery = remember { mutableStateOf("") }
    val isSearching = remember { mutableStateOf(false) }
    SearchBar(modifier = Modifier.padding(horizontal = 24.dp).clip(RoundedCornerShape(16.dp)),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery.value,
                onSearch = ::tempOnTextChange,
                onQueryChange = ::tempOnTextChange,
                expanded = isSearching.value,
                onExpandedChange = { isSearching.value = !isSearching.value },
                placeholder = { Text("Search Location", fontFamily = PoppinsFontFamily,
                    fontSize = 15.sp, color = LightGray, fontWeight = FontWeight.W400, lineHeight = 22.5.sp)
                },
                trailingIcon = { Icon(
                    Icons.Default.Search, contentDescription = null,
                    tint = LightGray
                ) },
            )
        },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        expanded = isSearching.value,
        onExpandedChange = {isSearching.value = !isSearching.value}) {

    }
}