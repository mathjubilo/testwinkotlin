package com.example.testwinkotlin.presentation.app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.presentation.components.AppButton
import com.example.testwinkotlin.presentation.components.AppText
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenEvents
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel
import com.example.testwinkotlin.presentation.navigation.graphs.AppNavHostView
import com.example.winkotlin.presentation.navigation.BottomBar
import com.example.winkotlin.presentation.navigation.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainView(
    intent: Intent?,
    currentDestination: String? = null,
    navigateToLaunchScreen: () -> Unit,
    navigateUp: () -> Unit,
    navigate: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {

        var title: MutableState<String> = remember {
            mutableStateOf("Active Incidents")
        }
        /*val navStackBackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navStackBackEntry?.destination?.route*/

        currentDestination?.let { route ->
            title.value = route
        }



        //Text(text = "the id is ${intent.getIntExtra("incId", 0)}")
        AppText(
            text = "Logged in with user ${IWinApi.USERNAME}",
            modifier = Modifier
                .padding(bottom = 10.dp)
        )
        AppButton(text = "Logout", modifier = Modifier
            .padding(horizontal = 20.dp)
            .clickable {
                LaunchScreenViewModel.initialEvent = LaunchScreenEvents.logout
                navigateToLaunchScreen()
            }
        )

        Scaffold(
            topBar = {

                TopBar(titleText = title, navigateUp = navigateUp)
            },
            bottomBar = {
                BottomBar(
                    navigate = navigate,
                    currentDestination = currentDestination
                )
            }
        ) {
                contentPadding ->
            // Screen content
            Box(modifier = Modifier.padding(contentPadding)) {
                AppNavHostView()
            }
        }
    }
}