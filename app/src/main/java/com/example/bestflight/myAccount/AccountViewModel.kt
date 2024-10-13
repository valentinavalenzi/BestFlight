package com.example.bestflight.myAccount

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestflight.data.PreferencesKeys
import com.example.bestflight.data.getFromDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.bestflight.data.saveToDataStore
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext val context: Context,
): ViewModel() {
    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    init {
        getNameFromDataStore()
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