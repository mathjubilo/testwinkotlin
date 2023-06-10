package com.example.testwinkotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.testwinkotlin.data.local.api.BiometricResponse
import com.example.testwinkotlin.presentation.navigation.RootNavHostView
import com.example.testwinkotlin.ui.theme.TestWinKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    var state: MutableState<String> = mutableStateOf("")
    //lateinit var launcher: ActivityResultLauncher<Intent>
    //var apiTests = APITests()
    // Hello


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //test()
        /*launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the returned Uri
            println("Kork the resultado es $result")
        }*/

        createNotificationChannel()

        setContent {
            TestWinKotlinTheme {
                RootNavHostView(
                    navController = rememberNavController(),
                    intent = intent
                )
            }
        }
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "notificationChannel"
            val descriptionText = "notificationChannelDescription"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("1", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}

interface TestResultApi {
    fun callLauncher()
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
