package com.example.testwinkotlin.presentation.incidentsView

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun IncidentsView(
    /*viewModel: IncidentsViewModel = hiltViewModel(),
    navigateToLoginView: (String) -> Unit*/
) {
    //val state = viewModel.state
    val mainActivity = LocalContext.current as FragmentActivity
    /*val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { activityResult ->
            viewModel.onBiometricsSettingsResponse(activityResult = activityResult)
        }
    )*/
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Incidents View")
        /*when (state) {
            IncidentsViewState.error -> {
                ErrorView(
                    errorMessage = "Error: ${viewModel.errorMessage}",
                    okAction = {
                        viewModel.event.set(LaunchScreenEvents.navigateToLoginViewAndShowLoginForm)
                    }
                )
            }
            IncidentsViewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    strokeWidth = Dp(value = 4F)
                )
            }
            IncidentsViewState.registerToPushNotifications -> {
                LaunchedEffect(true) {
                    viewModel.registerPushOn(
                        environment = "PRE",
                        activity = mainActivity
                    )
                }
            }
            IncidentsViewState.unregisterToPushNotifications -> {
                LaunchedEffect(true) {
                    viewModel.unregisterPushOn(
                        environment = "PRE",
                        activity = mainActivity
                    )
                }
            }
            IncidentsViewState.navigateToLoginView -> {
                LaunchedEffect(viewModel.state) {
                    navigateToLoginView(LoginViewModel.initialEvent)
                }
            }
            IncidentsViewState.navigateToAppView -> {
                LaunchedEffect(viewModel.state) {
                    navigateToAppView()
                }
            }
            IncidentsViewState.showDecryptBiometricWindow -> {
                LaunchedEffect(true) {
                    viewModel.authenticateToDecrypt(mainActivity)
                }
            }
            IncidentsViewState.showBiometricsSettings -> {
                LaunchedEffect(true) {
                    Log.d("IncidentsView", "Showing biometric settings")
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {

                    }
            IncidentsViewState.showEncryptBiometricWindow -> {
                LaunchedEffect(true) {
                    viewModel.authenticateToEncrypt(mainActivity)
                }       putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    activityLauncher.launch(enrollIntent)
                }
            }
            IncidentsViewState.loginWithBiometrics -> {
                LaunchedEffect(true) {
                    Log.d("IncidentsView", "Running Biometric login")
                    viewModel.biometricLogin(mainActivity)
                }
            }

            else -> {}*/
    }
}
