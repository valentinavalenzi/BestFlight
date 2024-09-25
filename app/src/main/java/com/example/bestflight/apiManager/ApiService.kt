package com.example.bestflight.apiManager

import com.example.bestflight.home.FlightModel
import retrofit.http.GET
import retrofit.Call

interface ApiService {
    @GET("availableflights")
    fun getFlightPrices(): Call<List<FlightModel>>
}