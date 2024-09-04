package com.example.bestflight.myTrips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bestflight.R
import com.example.bestflight.myTrips.upcomingTrips.UpcomingTripsViewModel
import com.example.bestflight.ui.theme.largeText
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyTrips() {
    val viewModel = hiltViewModel<UpcomingTripsViewModel>()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = (R.string.trips)),
            fontSize = largeText,
        )
    }
}