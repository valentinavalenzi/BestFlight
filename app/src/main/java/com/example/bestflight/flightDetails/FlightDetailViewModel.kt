package com.example.bestflight.flightDetails

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestflight.R
import com.example.bestflight.apiManager.ApiServiceImpl
import com.example.bestflight.home.FlightModel
import com.example.bestflight.security.BiometricAuthManager
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
    private val biometricAuthManager: BiometricAuthManager,
    savedStateHandle: SavedStateHandle
    ) : ViewModel() {

        private val flightId: String = savedStateHandle["flightId"] ?: ""

        private val _loadingFlight = MutableStateFlow(false)
        val loadingFlight = _loadingFlight.asStateFlow()

        private val _flight = MutableStateFlow<FlightModel?>(null)
        val flight = _flight.asStateFlow()

        private val _showRetry = MutableStateFlow(false)
        val showRetry = _showRetry.asStateFlow()

        private val _isBiometricAuthenticated = MutableStateFlow(false)
        val isBiometricAuthenticated = _isBiometricAuthenticated.asStateFlow()

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

        fun authenticateBiometrically(onSuccess: () -> Unit, activityContext: Context) {
            biometricAuthManager.authenticate(
                context = activityContext,
                onSuccess = {
                    _isBiometricAuthenticated.value = true
                    onSuccess()
                },
                onError = {
                    _isBiometricAuthenticated.value = false
                    Toast.makeText(
                        context,
                        ContextCompat.getString(context, R.string.auth_error),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFail = {
                    _isBiometricAuthenticated.value = false
                    Toast.makeText(
                        context,
                        ContextCompat.getString(context, R.string.auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

}