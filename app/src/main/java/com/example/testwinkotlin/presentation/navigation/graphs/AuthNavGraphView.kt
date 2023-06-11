package com.example.testwinkotlin.presentation.navigation.graphs

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.testwinkotlin.presentation.login.LoginView
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenView
import com.example.testwinkotlin.presentation.login.LoginViewEvent
import com.example.testwinkotlin.presentation.navigation.Screens

@RequiresApi(Build.VERSION_CODES.R)
fun NavGraphBuilder.AuthNavGraphView(
    navController: NavHostController,
    intent: Intent?
) {
    navigation(
        route = Screens.AuthNavGraphRoute.route,
        startDestination = Screens.LaunchScreen.route,
        ) {

        composable(Screens.LaunchScreen.route) {

            LaunchScreenView(
                navigateToLoginView = { event ->
                    navController.navigate("${Screens.Login.route}?event=${event}")
                },
                navigateToAppView = {
                    navController.navigate(Screens.MainNavGraphRoute.route)
                },
                intent = intent
            )
        }

        composable(
            route = "${Screens.Login.route}?event={event}",
            arguments = listOf(
                navArgument("event") {
                    defaultValue = LoginViewEvent.showLoginForm
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            LoginView(
                navigateToAppView = {
                    navController.navigate(Screens.MainNavGraphRoute.route)
                },
                navigateToLaunchScreenView = {
                    navController.navigate(Screens.LaunchScreen.route)
                }
            )
        }
    }
}