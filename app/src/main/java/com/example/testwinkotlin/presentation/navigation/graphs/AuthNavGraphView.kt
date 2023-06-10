package com.example.testwinkotlin.presentation.navigation.graphs

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.testwinkotlin.presentation.login.LoginView
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenView
import com.example.testwinkotlin.presentation.login.LoginViewEvent
import com.example.testwinkotlin.presentation.navigation.app.AppRoutes
import com.example.testwinkotlin.presentation.navigation.auth.AuthRoutes

@RequiresApi(Build.VERSION_CODES.R)
fun NavGraphBuilder.AuthNavGraphView(
    navController: NavHostController,
    intent: Intent?
) {
    navigation(
        route = AuthRoutes.auth,
        startDestination = AuthRoutes.launchScreen,
        ) {

        composable(AuthRoutes.launchScreen) {

            LaunchScreenView(
                navigateToLoginView = { event ->
                    navController.navigate("${AuthRoutes.login}?event=${event}")
                },
                navigateToAppView = {
                    navController.navigate(AppRoutes.app)
                },
                intent = intent
            )
        }

        composable(
            route = "${AuthRoutes.login}?event={event}",
            arguments = listOf(
                navArgument("event") {
                    defaultValue = LoginViewEvent.showLoginForm
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            LoginView(
                navigateToAppView = {
                    navController.navigate(AppRoutes.app)
                },
                navigateToLaunchScreenView = {
                    navController.navigate(AuthRoutes.launchScreen)
                }
            )
        }
    }
}