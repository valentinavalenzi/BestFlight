package com.example.bestflight.apiManager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bestflight.R
import com.example.bestflight.home.FlightModel
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    fun getFlights(
        context: Context,
        onSuccess: (List<FlightModel>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                context.getString(R.string.flight_prices_url)
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<List<FlightModel>> = service.getFlightPrices()
        call.enqueue(object : Callback<List<FlightModel>> {
            override fun onResponse(response: Response<List<FlightModel>>?, retrofit: Retrofit?) {
                loadingFinished()
                if (response?.isSuccess == true) {
                    val flights: List<FlightModel> = response.body()
                    onSuccess(flights)
                } else {
                    onFailure(Exception("Bad request"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, R.string.cant_get_flights, Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }

    fun getFlightById(
        context: Context,
        flightId: String,
        onSuccess: (FlightModel) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                context.getString(R.string.flight_prices_url)
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<FlightModel> = service.getFlightData(flightId)
        call.enqueue(object : Callback<FlightModel> {
            override fun onResponse(response: Response<FlightModel>?, retrofit: Retrofit?) {
                loadingFinished()
                if (response?.isSuccess == true) {
                    val flight: FlightModel = response.body()
                    onSuccess(flight)
                } else {
                    onFailure(Exception("Bad request"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, R.string.cant_get_flight, Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}