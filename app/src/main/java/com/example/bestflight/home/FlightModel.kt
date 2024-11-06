package com.example.bestflight.home

import java.sql.Time
import java.util.Date

data class FlightModel(
    val id: String,
    val from: String,
    val to: String,
    val from_name: String,
    val to_name: String,
    val departure_time: String,
    val arrival_time: String,
    val flight_duration: String,
    val stops_number: String,
    val flight_number: String,
    val destination_img: String,
    val included_baggage: String,
    val price: String,
)