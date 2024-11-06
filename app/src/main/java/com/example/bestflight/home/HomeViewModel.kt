package com.example.bestflight.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestflight.apiManager.ApiServiceImpl
import com.example.bestflight.data.PreferencesKeys
import com.example.bestflight.data.getFromDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _fromText = MutableStateFlow("")
    private val _toText = MutableStateFlow("")

    init {
        loadFlights()
        getNameFromDataStore()
        viewModelScope.launch {
            combine(_flights, _fromText, _toText) { flights, fromText, toText ->
                flights.filter { flight ->
                    (fromText.isEmpty() || flight.from.contains(fromText, ignoreCase = true) || flight.from_name.contains(fromText, ignoreCase = true)) &&
                            (toText.isEmpty() || flight.to.contains(toText, ignoreCase = true) || flight.to_name.contains(toText, ignoreCase = true))
                }
            }.collect {
                _filteredFlights.value = it
            }
        }
    }

    fun retryLoadingFlights() {
        loadFlights()
    }

    fun onSearchFromTextChanged(text: String) {
        _fromText.value = text
    }

    fun onSearchDestinationTextChanged(text: String) {
        _toText.value = text
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

    private fun getNameFromDataStore() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.USER_NAME_KEY).collect {
                _userName.value = it ?: ""
            }
        }
    }
    fun saveToDataStore(username: String) {
        viewModelScope.launch {
            com.example.bestflight.data.saveToDataStore(context, username, PreferencesKeys.USER_NAME_KEY)
            _userName.value = username
        }
    }

}