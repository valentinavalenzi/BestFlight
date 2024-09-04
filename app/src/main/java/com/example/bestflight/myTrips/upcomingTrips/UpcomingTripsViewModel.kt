package com.example.bestflight.myTrips.upcomingTrips

import androidx.lifecycle.ViewModel
import com.example.bestflight.myTrips.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UpcomingTripsViewModel @Inject constructor() : ViewModel() {

    private var _upcomingTripsList = MutableStateFlow(listOf<Trip>())
    val upcomingTripsList = _upcomingTripsList.asStateFlow()

//    fun addFriend(name: String, email: String, age: String) {
//        val friend = Friend(name, email, age)
//        val newList = _friendList.value + friend
//        viewModelScope.launch {
//            _friendList.emit(newList)
//        }
//    }
}