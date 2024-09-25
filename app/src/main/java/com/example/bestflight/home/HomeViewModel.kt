package com.example.bestflight.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestflight.apiManager.ApiServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl,
) : ViewModel() {

    private val _loadingFlights = MutableStateFlow(false)
    val loadingFlights = _loadingFlights.asStateFlow()

    private val _flights = MutableStateFlow(listOf<FlightModel>())
    val flights = _flights.asStateFlow()

    private val _filteredFlights = MutableStateFlow(listOf<FlightModel>())
    val filteredFlights = _filteredFlights.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        loadFlights()
    }

    fun retryLoadingRanking() {
        loadFlights()
    }

    fun onSearchTextChanged(text: String) {
        val updatedFlights = _flights.value.filter { flight ->
            flight.to.contains(text, ignoreCase = true)
        }
        _filteredFlights.value = updatedFlights
    }

    private fun loadFlights() {
        _loadingFlights.value = true
        apiServiceImpl.getFlights(
            context = context,
            onSuccess = {
                viewModelScope.launch {
                    _flights.emit(it)
                    _filteredFlights.emit(it)
                }
                _showRetry.value = false
            },
            onFail = {
                _showRetry.value = true
            },
            loadingFinished = {
                _loadingFlights.value = false
            }
        )
    }
}