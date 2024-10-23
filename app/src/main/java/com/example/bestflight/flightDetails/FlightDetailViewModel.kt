package com.example.bestflight.flightDetails

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestflight.apiManager.ApiServiceImpl
import com.example.bestflight.home.FlightModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightDetailViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: ApiServiceImpl,
    savedStateHandle: SavedStateHandle
    ) : ViewModel() {

        private val flightId: String = savedStateHandle["flightId"] ?: ""

        private val _loadingFlight = MutableStateFlow(false)
        val loadingFlight = _loadingFlight.asStateFlow()

        private val _flight = MutableStateFlow<FlightModel?>(null)
        val flight = _flight.asStateFlow()

        private val _showRetry = MutableStateFlow(false)
        val showRetry = _showRetry.asStateFlow()

        fun retryLoadingFlight(flightId: String) {
            loadFlight(flightId)
        }

        init {
            loadFlight(flightId = flightId)
        }

        private fun loadFlight(flightId: String) {
            _loadingFlight.value = true
            apiServiceImpl.getFlightById(
                context = context,
                flightId = flightId,
                onSuccess = {
                    viewModelScope.launch {
                        _flight.emit(it)
                    }
                    _showRetry.value = false
                },
                onFail = {
                    _showRetry.value = true
                },
                loadingFinished = {
                    _loadingFlight.value = false
                }
            )
        }
    }