package com.example.bestflight.myTrips

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
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
            apiServiceImpl.getFlightById(
                context = context,
                flightId = flightId,
                onSuccess = { flightModel ->
                    viewModelScope.launch {
                        // Create a Trip from the flight details
                        val trip = Trip(
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
                    }
                },
                onFail = {
                    // Handle error here (e.g., show a retry option)
                },
                loadingFinished = {
                    // Handle loading finished, if needed
                }
            )
        }
    }
}
