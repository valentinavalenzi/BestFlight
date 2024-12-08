package com.example.bestflight.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.bestflight.data.BestFlightDatabase
import com.example.bestflight.data.Card
import com.example.bestflight.data.PreferencesKeys
import com.example.bestflight.data.getFromDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext val context: Context,
): ViewModel() {
    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val bestFlightDatabase = BestFlightDatabase.getDatabase(context)
    val cardsList = bestFlightDatabase.cardDao().getAllCards().asFlow()

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

    fun addCard(cardNumber: String, cardType: String, expirationDate: String, cvv: String) {
        val newCard = Card(cardNumber = cardNumber, cardType = cardType, expirationDate = expirationDate, cvv = cvv)
        viewModelScope.launch {
            bestFlightDatabase.cardDao().insert(newCard)
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            bestFlightDatabase.cardDao().delete(card)
        }
    }

}