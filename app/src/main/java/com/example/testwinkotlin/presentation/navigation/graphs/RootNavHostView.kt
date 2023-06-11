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
import com.example.testwinkotlin.presentation.navigation.graphs.AuthNavGraphView

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RootNavHostView(
    navController: NavHostController,
    intent: Intent? = null
) {

    NavHost(
        navController = navController,
        route = Screens.RootNavGraphRoute.route,
        startDestination = Screens.AuthNavGraphRoute.route
    ) {

        AuthNavGraphView(
            navController = navController,
            intent = intent
        )
        composable(Screens.MainNavGraphRoute.route) {
            MainView(
                intent = intent,
                navController = navController
            )
        }
    }
}


