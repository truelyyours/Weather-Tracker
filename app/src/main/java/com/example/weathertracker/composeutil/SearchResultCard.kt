package com.example.weathertracker.composeutil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.example.weathertracker.ui.theme.BackgroundGray
import com.example.weathertracker.ui.theme.CustomBlack
import com.example.weathertracker.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchResultCard(cityName: String, temp: String, imageUrl: String) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(117.dp).width(336.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundGray)) {
        Column(modifier = Modifier.padding(start = 31.dp)) {
            Text(text = cityName,
                fontFamily = PoppinsFontFamily, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = CustomBlack
            )
            Text(text = temp,
                fontFamily = PoppinsFontFamily, fontSize = 60.sp, fontWeight = FontWeight.Medium, color = CustomBlack
            )

        }

        GlideSubcomposition(modifier = Modifier.size(123.dp),
            model = "https:$imageUrl"
        ) {
            when (state) {
                RequestState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.size(65.dp), color = Color.Cyan
                )
                RequestState.Failure -> Icon(
                    Icons.Default.Warning,
                    tint = Color.Red, contentDescription = null)
                is RequestState.Success -> Image(painter = painter,
                    modifier = Modifier.size(85.dp),
                    contentDescription = "Weather Icon Image")
            }
        }    }
}