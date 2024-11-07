package com.example.bestflight.myTrips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestflight.R
import com.example.bestflight.data.Trip
import com.example.bestflight.ui.theme.DarkBlue
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.superLargeText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import com.example.bestflight.ui.theme.Black
import com.example.bestflight.ui.theme.BlueGrey80
import com.example.bestflight.ui.theme.DeepBlue
import com.example.bestflight.ui.theme.PastelBlue
import com.example.bestflight.ui.theme.Red
import com.example.bestflight.ui.theme.White
import com.example.bestflight.ui.theme.size16dp

@Composable
fun MyTrips(onNavigateToFlightDetail: (String) -> Unit) {
    val viewModel = hiltViewModel<MyTripsViewModel>()
    val tripsList by viewModel.tripList.collectAsState(initial = listOf())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = (R.string.trips)),
            fontSize = superLargeText,
            color = White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                lineHeight = 50.sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier.padding(top = 24.dp)
        )

        if (tripsList.isEmpty()) {
            Text(text = stringResource(id = R.string.no_trips))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tripsList) { trip ->
                    TripCard(
                        trip = trip,
                        onDelete = { viewModel.deleteTrip(trip) },
                        onClick = { onNavigateToFlightDetail(trip.id.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
fun TripCard(trip: Trip, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = size16dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = PastelBlue)
    ) {
        Row(
            modifier = Modifier
                .padding(size16dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.destination) + " ${trip.to_name}",
                    fontSize = largeText,
                    color = DeepBlue
                )
                Text(
                    text = stringResource(id = R.string.from) + " ${trip.from}",
                    color = DeepBlue
                )
                Text(text = trip.departure_time, color = BlueGrey80)
            }
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Trip",
                    tint = Red
                )
            }
        }
    }
}

@Preview
@Composable
fun TripCardPreview() {
    val sampleTrip = Trip(
        id = 1,
        from = "New York",
        to = "London",
        to_name = "London",
        departure_time = "10:00 AM",
        arrival_time = "10:00 PM",
        flight_duration = "8 hours",
        stops_number = "Non-stop",
        flight_number = "NY123",
        included_baggage = "2 pieces"
    )

    TripCard(
        trip = sampleTrip,
        onDelete = { },
        onClick = { }
    )
}
