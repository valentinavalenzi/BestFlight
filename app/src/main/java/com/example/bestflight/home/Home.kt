package com.example.bestflight.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestflight.R
import com.example.bestflight.components.FlightCard
import com.example.bestflight.components.SearchBar
import com.example.bestflight.ui.theme.Blue
import com.example.bestflight.ui.theme.DarkBlue
import com.example.bestflight.ui.theme.White
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.size10dp
import com.example.bestflight.ui.theme.size20dp
import com.example.bestflight.ui.theme.size24dp
import com.example.bestflight.ui.theme.size50sp
import com.example.bestflight.ui.theme.size64dp
import com.example.bestflight.ui.theme.smallText
import com.example.bestflight.ui.theme.superLargeText

@Composable
fun Home(onNavigateToFlight: (String) -> Unit) {

    val viewModel = hiltViewModel<HomeViewModel>()
    val userName by viewModel.userName.collectAsState()
    val flights by viewModel.filteredFlights.collectAsState()
    val loading by viewModel.loadingFlights.collectAsState()
    val showRetry by viewModel.showRetry.collectAsState()

    HomeContent(
        userName = userName,
        flights = flights,
        loading = loading,
        showRetry = showRetry,
        onSearchFromChanged = { viewModel.onSearchFromTextChanged(it) },
        onSearchDestinationChanged = { viewModel.onSearchDestinationTextChanged(it) },
        onRetry = { viewModel.retryLoadingFlights() },
        onNavigateToFlight = onNavigateToFlight,
        onSaveUserName = { viewModel.saveToDataStore(it) }
    )
}

@Composable
fun HomeContent(
    userName: String = "",
    flights: List<FlightModel> = emptyList(),
    loading: Boolean = false,
    showRetry: Boolean = false,
    onSearchFromChanged: (String) -> Unit = {},
    onSearchDestinationChanged: (String) -> Unit = {},
    onRetry: () -> Unit = {},
    onNavigateToFlight: (String) -> Unit = {},
    onSaveUserName: (String) -> Unit = {}
) {
    var searchFromText by remember { mutableStateOf("") }
    var searchDestinationText by remember { mutableStateOf("") }
    var userNameLocal by remember { mutableStateOf("") }

    if (userName.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(size10dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.set_user_name),
                fontSize = largeText,
                fontWeight = FontWeight.Bold,
            )
            TextField(
                value = userNameLocal,
                onValueChange = { userNameLocal = it },
                label = {
                    Text(text = stringResource(id = R.string.name_example))
                },
            )
            Button(
                modifier = Modifier.background(color = Color.Transparent),
                onClick = { onSaveUserName(userNameLocal) }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(size24dp),
        ) {
            Text(
                text = stringResource(id = R.string.welcome) + ", " + userName,
                fontSize = smallText,
                color = White
            )
            Text(
                text = stringResource(id = (R.string.home_greeting)),
                fontSize = superLargeText,
                color = White,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    lineHeight = size50sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = stringResource(id = R.string.from), fontSize = largeText, color = White)
            SearchBar(
                onChange = {
                    searchFromText = it
                    onSearchFromChanged(it)
                },
                onClear = {
                    searchFromText = ""
                    onSearchFromChanged("")
                }
            )
            Spacer(modifier = Modifier.height(size20dp))
            Text(text = stringResource(id = R.string.to2), fontSize = largeText, color = White)
            SearchBar(
                onChange = {
                    searchDestinationText = it
                    onSearchDestinationChanged(it)
                },
                onClear = {
                    searchDestinationText = ""
                    onSearchDestinationChanged("")
                }
            )
            Spacer(modifier = Modifier.height(size20dp))

            Spacer(modifier = Modifier.height(size20dp))

            when {
                loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(size64dp)
                                .align(Alignment.Center),
                            color = Blue,
                            trackColor = DarkBlue,
                        )
                    }
                }

                showRetry -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            size10dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.cant_get_flights),
                            fontSize = largeText,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = White,
                        )
                        Button(onClick = onRetry) {
                            Text(text = stringResource(id = R.string.try_again))
                        }
                    }
                }

                else -> {
                    LazyColumn {
                        items(flights) { flight ->
                            FlightsView(
                                flight = flight,
                                onClick = { onNavigateToFlight(flight.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightsView(
    flight: FlightModel,
    onClick: () -> Unit
) {
    FlightCard(
        flight = flight,
        onClick = onClick
    )
}

@Preview
@Composable
fun HomePreview() {
    HomeContent(
        userName = "Preview User",
        flights = listOf(
            FlightModel(
                id = "1",
                from = "JFK",
                to = "MIA",
                from_name = "New York",
                to_name = "Miami",
                departure_time = "10:00 AM",
                arrival_time =  "8:30pm",
                flight_duration = "5hs3min",
                flight_number = "AA221",
                destination_img = "",
                included_baggage = "12kg",
                price = "200",
                stops_number = "2"
            ),
            FlightModel(
                id = "2",
                from = "LAX",
                to = "MIA",
                from_name = "Los Angeles",
                to_name = "Miami",
                departure_time = "10:00 AM",
                arrival_time =  "8:30pm",
                flight_duration = "5hs3min",
                flight_number = "AA235",
                destination_img = "",
                included_baggage = "12kg",
                price = "2999",
                stops_number = "1"
            )
        )
    )
}