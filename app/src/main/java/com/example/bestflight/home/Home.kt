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
import androidx.compose.ui.res.colorResource
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
import com.example.bestflight.myAccount.AccountViewModel
import com.example.bestflight.ui.theme.Blue
import com.example.bestflight.ui.theme.DarkBlue
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.smallText
import com.example.bestflight.ui.theme.superLargeText

@Composable
fun Home( onNavigateToFlight: (String) -> Unit) {

    var searchText by remember { mutableStateOf("") }
    val viewModel = hiltViewModel<HomeViewModel>()

    val accountViewModel = hiltViewModel<AccountViewModel>()
    val userName by accountViewModel.userName.collectAsState()
    var userNameLocal by remember {
        mutableStateOf("")
    }

    if (userName.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
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
                onClick = { accountViewModel.saveToDataStore(userNameLocal) }) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    } else {
        // Main container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(24.dp),
        ) {
            // Greeting
            Text(
                text = stringResource(id = R.string.welcome) + ", " + userName,
                fontSize = smallText,
                color = colorResource(id = R.color.white)
            )
            Text(
                text = stringResource(id = (R.string.home_greeting)),
                fontSize = superLargeText,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    lineHeight = 50.sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                onChange = {
                    searchText = it
                    viewModel.onSearchTextChanged(it)
                },
                onClear = {
                    searchText = ""
                    viewModel.onSearchTextChanged("")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

            val flights by viewModel.filteredFlights.collectAsState()
            val loading by viewModel.loadingFlights.collectAsState()
            val showRetry by viewModel.showRetry.collectAsState()

            Spacer(modifier = Modifier.height(20.dp))

            // Display flight list based on loading state
            when {
                loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center),
                            color = Blue,
                            trackColor = DarkBlue,
                        )
                    }
                }

                showRetry -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.cant_get_flights),
                            fontSize = largeText,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                        )
                        Button(onClick = { viewModel.retryLoadingFlights() }) {
                            stringResource(id = R.string.try_again)
                        }
                    }
                }

                else -> {
                    LazyColumn {
                        items(flights) { flight ->
                            FlightsView(flight = flight, onClick = { onNavigateToFlight(flight.id) })
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

}