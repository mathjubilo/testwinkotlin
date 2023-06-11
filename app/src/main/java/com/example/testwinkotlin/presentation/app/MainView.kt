package com.example.testwinkotlin.presentation.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testwinkotlin.presentation.navigation.graphs.MainNavGraph
import com.example.winkotlin.presentation.navigation.BottomBar
import com.example.winkotlin.presentation.navigation.TopBar


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainView(
    intent: Intent? = null,
    navController: NavHostController = rememberNavController()
){
    var title: MutableState<String> = remember {
        mutableStateOf("Active Incidents")
    }

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination?.route

    //Text("Logged in as ${IWinApi.USERNAME}")
    Scaffold(
        topBar = {
            TopBar(
                titleText = title,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },

        bottomBar = {
            BottomBar(
                navigate = { route ->
                    navController.navigate(route)
                },
                currentDestination = currentDestination
            )
        }
    ) {
            contentPadding ->
        // Screen content
        Box(modifier = Modifier.padding(contentPadding)) {
            MainNavGraph(navController = navController)
        }
    }
}