package com.example.bestflight.apiManager

import com.example.bestflight.home.FlightModel
import retrofit.http.GET
import retrofit.Call
import retrofit.http.Path

interface ApiService {
    @GET("availableflights")
    fun getFlightPrices(): Call<List<FlightModel>>
    @GET("availableflights/{flightId}")
    fun getFlightData(@Path("flightId") flightId: String): Call<FlightModel>
}