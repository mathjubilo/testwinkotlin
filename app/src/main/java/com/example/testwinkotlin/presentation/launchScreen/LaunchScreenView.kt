package com.example.testwinkotlin.presentation.launchScreen

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testwinkotlin.presentation.components.AppText
import com.example.testwinkotlin.presentation.components.ErrorView
import com.example.testwinkotlin.presentation.login.LoginViewModel
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenEvents.*

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LaunchScreenView(
    viewModel: LaunchScreenViewModel = hiltViewModel(),
    navigateToLoginView: (String) -> Unit,
    navigateToAppView: () -> Unit,
    intent: Intent?)
{
    val state = viewModel.state
    val mainActivity = LocalContext.current as FragmentActivity
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { activityResult ->
            viewModel.onBiometricsSettingsResponse(activityResult = activityResult)
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (state) {
            LaunchScreenViewState.error -> {
                ErrorView(
                    errorMessage = "Error: ${viewModel.errorMessage}",
                    okAction = {
                        viewModel.event.set(LaunchScreenEvents.navigateToLoginViewAndShowLoginForm)
                    }
                )
            }
            LaunchScreenViewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    strokeWidth = Dp(value = 4F)
                )
            }
            LaunchScreenViewState.registerToPushNotifications -> {
                LaunchedEffect(true) {
                    viewModel.registerPushOn(
                        environment = "PRE",
                        activity = mainActivity
                    )
                }
            }
            LaunchScreenViewState.unregisterToPushNotifications -> {
                LaunchedEffect(true) {
                    viewModel.unregisterPushOn(
                        environment = "PRE",
                        activity = mainActivity
                    )
                }
            }
            LaunchScreenViewState.navigateToLoginView -> {
                LaunchedEffect(viewModel.state) {
                    navigateToLoginView(LoginViewModel.initialEvent)
                }
            }
            LaunchScreenViewState.navigateToAppView -> {
                LaunchedEffect(viewModel.state) {
                    navigateToAppView()
                }
            }
            LaunchScreenViewState.showEncryptBiometricWindow -> {
                LaunchedEffect(true) {
                    viewModel.authenticateToEncrypt(mainActivity)
                }
            }
            LaunchScreenViewState.showDecryptBiometricWindow -> {
                LaunchedEffect(true) {
                    viewModel.authenticateToDecrypt(mainActivity)
                }
            }
            LaunchScreenViewState.showBiometricsSettings -> {
                LaunchedEffect(true) {
                    Log.d("LaunchScreenView", "Showing biometric settings")
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    activityLauncher.launch(enrollIntent)
                }
            }
            LaunchScreenViewState.loginWithBiometrics -> {
                LaunchedEffect(true) {
                    Log.d("LaunchScreenView", "Running Biometric login")
                    viewModel.biometricLogin(mainActivity)
                }
            }

            else -> {}
        }
    }
}