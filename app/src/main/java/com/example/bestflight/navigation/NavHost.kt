package com.example.bestflight.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.example.bestflight.flightDetails.FlightDetail
import com.example.bestflight.home.Home
import com.example.bestflight.myAccount.Account
import com.example.bestflight.myTrips.MyTrips
import navigation.BestFlightScreen

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BestFlightScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),

    ) {
        composable(route = BestFlightScreen.Home.name) {
            Home(onNavigateToFlight = { flightId ->
                navController.navigate("${BestFlightScreen.FlightDetail.name}/$flightId")
            })
        }
        composable(route = BestFlightScreen.MyTrips.name) {
            MyTrips()
        }
        composable(route = BestFlightScreen.Account.name) {
            Account()
        }
        composable(
            route = "${BestFlightScreen.FlightDetail.name}/{flightId}",
        ) { backStackEntry ->
            val flightId = backStackEntry.arguments?.getString("flightId")
            flightId?.let { id ->
                FlightDetail(flightId = id)
            }
        }

    }
}