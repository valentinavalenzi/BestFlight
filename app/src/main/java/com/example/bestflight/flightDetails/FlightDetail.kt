package com.example.bestflight.flightDetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bestflight.R
import com.example.bestflight.myTrips.MyTripsViewModel
import com.example.bestflight.ui.theme.Blue
import com.example.bestflight.ui.theme.largeText
import com.example.bestflight.ui.theme.mediumText
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.navigation.NavController
import com.example.bestflight.data.Trip


@Composable
fun FlightDetail(flightId: String, navController: NavController) {

    val flightDetailViewModel = hiltViewModel<FlightDetailViewModel>()
    val flightDetail by flightDetailViewModel.flight.collectAsState()

    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }

    val myTripsViewModel =
        hiltViewModel<MyTripsViewModel>() // when purchased, we need to add a trip to My Trips

    val tripsList by myTripsViewModel.tripList.collectAsState(initial = listOf())
    val isTripInMyTrips = tripsList.any { it.id.toString() == flightId }
    val trip: Trip? = tripsList.find { it.id.toString() == flightId }
    flightDetail?.let { flight ->
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AsyncImage(
                    model = flight.destination_img,
                    contentDescription = "Destination Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.flight_from) + " ${flight.from} " + stringResource(
                            id = R.string.to
                        ) + " ${flight.to_name}",
                        fontSize = largeText,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                FlightInfoRow(
                    label = stringResource(id = R.string.flight_number),
                    value = flight.flight_number
                )
                FlightInfoRow(
                    label = stringResource(id = R.string.departure_time),
                    value = flight.departure_time
                )
                FlightInfoRow(
                    label = stringResource(id = R.string.arrival_time),
                    value = flight.arrival_time
                )
                FlightInfoRow(
                    label = stringResource(id = R.string.duration),
                    value = flight.flight_duration
                )
                FlightInfoRow(
                    label = stringResource(id = R.string.stops),
                    value = flight.stops_number
                )
                FlightInfoRow(
                    label = stringResource(id = R.string.baggage_included),
                    value = flight.included_baggage
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price and Purchase Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(id = R.string.price) + " \$${flight.price}",
                            fontSize = largeText,
                            fontWeight = FontWeight.Bold,
                            color = Blue
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (isTripInMyTrips) {
                                    myTripsViewModel.deleteTrip(trip!!)
                                    navController.popBackStack()
                                } else {
                                    when (isBiometricAvailable) {
                                        BiometricManager.BIOMETRIC_SUCCESS -> {
                                            flightDetailViewModel.authenticateBiometrically(
                                                onSuccess = {
                                                    myTripsViewModel.addTrip(flightId)
                                                    navController.popBackStack()
                                                },
                                                activityContext = context
                                            )
                                        }

                                        else -> {//TODO fix string here, should be error on purchasing also, instead of auth
                                            Toast.makeText(
                                                context,
                                                "Biometric authentication not supported.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(0.7f)
                        ) {
                            Text(
                                text = if (isTripInMyTrips) stringResource(id = R.string.delete_trip)
                                else stringResource(id = R.string.purchase_flight)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = mediumText,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = mediumText,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

//@Preview
//@Composable
//fun FlightDetailPreview() {
//    FlightDetail(flightId = "1")
//}
