package com.example.bestflight.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val from: String,
    val to: String,
    val to_name: String,
    val departure_time: String,
    val arrival_time: String,
    val flight_duration: String,
    val stops_number: String,
    val flight_number: String,
    val included_baggage: String,
)

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cardNumber: String,
    val cardType: String,
    val expirationDate: String,
    val cvv: String
)