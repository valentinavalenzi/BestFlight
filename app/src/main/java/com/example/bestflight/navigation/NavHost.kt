package com.example.bestflight.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import com.example.bestflight.home.Home
import com.example.bestflight.myAccount.Account
import com.example.bestflight.myTrips.MyTrips
import navigation.BestFlightScreen
import com.example.bestflight.notifications.Notifications

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BestFlightScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(route = BestFlightScreen.Home.name) {
            Home()
        }
        composable(route = BestFlightScreen.MyTrips.name) {
            MyTrips()
        }
        composable(route = BestFlightScreen.Notifications.name) {
            Notifications()
        }
        composable(route = BestFlightScreen.Account.name) {
            Account()
        }
    }
}