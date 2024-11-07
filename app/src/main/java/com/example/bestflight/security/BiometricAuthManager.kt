package com.example.bestflight.security

import javax.inject.Inject
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthManager @Inject constructor() {

    fun authenticate(context: Context, onError: () -> Unit, onSuccess: () -> Unit, onFail: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // handle authentication error here
                    onError()
                }
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // handle authentication success here
                    onSuccess()
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // handle authentication failure here
                    onFail()
                }
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            //TODO: fix these strings
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credentials")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}