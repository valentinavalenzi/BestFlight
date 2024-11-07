package com.example.bestflight.myTrips

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.bestflight.R
import com.example.bestflight.apiManager.ApiServiceImpl
import com.example.bestflight.data.BestFlightDatabase
import com.example.bestflight.data.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTripsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val apiServiceImpl: ApiServiceImpl
) : ViewModel() {

    private val bestFlightDatabase = BestFlightDatabase.getDatabase(context)
    val tripList: Flow<List<Trip>> = bestFlightDatabase.tripDao().getMyTrips().asFlow()

    fun addTrip(flightId: String) {
        viewModelScope.launch {
            val existingTrip = bestFlightDatabase.tripDao().getTripByFlightId(flightId)
            if (existingTrip == null) {
                apiServiceImpl.getFlightById(
                    context = context,
                    flightId = flightId,
                    onSuccess = { flightModel ->
                        viewModelScope.launch {
                            // Create a Trip from the flight details
                            val trip = Trip(
                                id = flightId.toInt(),
                                from = flightModel.from,
                                to = flightModel.to,
                                to_name = flightModel.to_name,
                                departure_time = flightModel.departure_time,
                                arrival_time = flightModel.arrival_time,
                                flight_duration = flightModel.flight_duration,
                                stops_number = flightModel.stops_number,
                                flight_number = flightModel.flight_number,
                                included_baggage = flightModel.included_baggage
                            )
                            bestFlightDatabase.tripDao().insert(trip)
                            //TODO fix string here
                            Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFail = {
                        Toast.makeText(context, stringResource(id = R.string.error) + stringResource(id = R.string.try_again), Toast.LENGTH_SHORT).show()
                    },
                    loadingFinished = {
                        // Handle loading finished, if needed
                    }
                )
            } else { //TODO fix string here
                Toast.makeText(context, "You have already bought this ticket", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            bestFlightDatabase.tripDao().delete(trip)
            // TODO: fix this string
            Toast.makeText(context, "Trip deleted successfully!", Toast.LENGTH_SHORT).show()
        }
    }

}
