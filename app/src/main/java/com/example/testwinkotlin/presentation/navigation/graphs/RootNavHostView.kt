package com.example.testwinkotlin.presentation.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.testwinkotlin.presentation.app.MainView
import com.example.testwinkotlin.presentation.navigation.app.AppRoutes
import com.example.testwinkotlin.presentation.navigation.graphs.AuthNavGraphView
import com.example.testwinkotlin.presentation.navigation.auth.AuthRoutes
import com.example.testwinkotlin.presentation.navigation.root.RootRoutes

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RootNavHostView(
    navController: NavHostController,
    intent: Intent? = null
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination?.route

    NavHost(
        navController = navController,
        route = RootRoutes.root,
        startDestination = RootRoutes.auth
    ) {

        AuthNavGraphView(
            navController = navController,
            intent = intent
        )
        composable(AppRoutes.app) {
            MainView(
                intent = intent,
                currentDestination = currentDestination,
                /*navigateToIncidentsView = { event ->
                    navController.navigate("${AppRoutes.activeIncidents}?event=${event}")
                },*/
                navigateToLaunchScreen = {
                    navController.navigate(AuthRoutes.launchScreen)
                },
                navigateUp = { navController.navigateUp() },
                navigate = { value ->
                    println("Value is ${value}")
                    navController.navigate(value)
                }
            )
        }
    }
}