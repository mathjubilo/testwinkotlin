package com.example.winkotlin.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.testwinkotlin.presentation.navigation.Screens

@Composable
fun BottomBar(
    navigate: (String)-> Unit,
    currentDestination: String?
    /*topBarTitle: MutableState<String>*/
) {

    val verticalPadding = 10.dp
    /*
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination
    topBarTitle.value = currentDestination?.route.toString().replace("_"," ")
*/
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .height(65.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                horizontal = verticalPadding
            )


    ) {

        BottomBarItem(
            title = "Active Incidents",
            image = com.example.testwinkotlin.R.drawable.logo,
            screen = Screens.ActiveIncidentsScreen.route,
            currentDestination = currentDestination,
            navigate = navigate
        )
        BottomBarItem(
            title = "Followed Incidents",
            image = com.example.testwinkotlin.R.drawable.logo,
            screen = Screens.FollowedIncidentsScreen.route,
            currentDestination = currentDestination,
            navigate = navigate
        )
        BottomBarItem(
            title = "Distribution Centers",
            image = com.example.testwinkotlin.R.drawable.logo,
            screen = Screens.DistributionCentersScreen.route,
            currentDestination = currentDestination,
            navigate = navigate
        )
        BottomBarItem(
            title = "My Profirle",
            image = com.example.testwinkotlin.R.drawable.logo,
            screen = Screens.MyProfileScreen.route,
            currentDestination = currentDestination,
            navigate = navigate
        )
    }
}

fun isFilterButton(screenRoute: String): Boolean {
    return screenRoute.equals("filters")
}

