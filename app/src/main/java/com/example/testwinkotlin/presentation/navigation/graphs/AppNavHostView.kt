package com.example.testwinkotlin.presentation.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testwinkotlin.presentation.incidentsView.IncidentsView
import com.example.testwinkotlin.presentation.navigation.Screens

class AppNavHostView  {
}
@Composable
fun MainNavGraph(navController: NavHostController) {

    // El Nav Host es el contenido
    NavHost(
        navController = navController,
        startDestination = Screens.ActiveIncidentsScreen.route,
        route = Screens.MainNavGraphRoute.route
    ) {
        composable(Screens.ActiveIncidentsScreen.route) {
            IncidentsView(
                /*navigate = { route ->
                    navController.navigate(route)
                }*/
            )
        }
        composable(Screens.FollowedIncidentsScreen.route) {
            IncidentsView()
        }
        composable(Screens.Filters.route) {
            IncidentsView()
        }
        composable(Screens.DistributionCentersScreen.route) {
            IncidentsView()
        }
        composable(Screens.MyProfileScreen.route) {
            IncidentsView()
        }


        // Esto va a tener que ser otro navhostview con dos vistas, la de detalles y la de mensajes
        composable(Screens.IncidentDetailsScreen.route+"{incId}") {
            //IncidentDetailsView(/*it.arguments?.getString("incId"),*/ navController)
        }
        composable(Screens.MessagesAndWorklogsScreen.route) {
            //MessagesAndWorklogs(navController)
        }
        /*composable(ScreensInfo.DistributionCenterDetailScreen.screen_route) {
            DistributionCenterDetailScreen()
        }
        composable(ScreensInfo.AppAndUserInfoScreen.screen_route) {
            AppAndUserInfoScreen()
        }
        composable(ScreensInfo.SettingsScreen.screen_route) {
            SettingsScreen()
        }
        composable(ScreensInfo.NotificationsSettingsScreen.screen_route) {
            NotificationsSettingsScreen()
        }*/
    }
}