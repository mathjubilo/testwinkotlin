package com.example.testwinkotlin.presentation.launchScreen

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwinkotlin.data.local.api.BiometricResponse
import com.example.testwinkotlin.data.local.api.BiometricsApi
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.data.remote.api.PushServiceApi
import com.example.testwinkotlin.domain.model.*
import com.example.testwinkotlin.utils.AsyncTaskResponse
import com.example.testwinkotlin.domain.useCases.*
import com.example.testwinkotlin.presentation.login.LoginViewEvent
import com.example.testwinkotlin.presentation.login.LoginViewModel
import com.example.testwinkotlin.utils.AppFilters
import com.example.testwinkotlin.utils.toUserAppFiltersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenEvents.*

@HiltViewModel
class LaunchScreenViewModel
@Inject constructor(
    private val getTokensFromDeviceUC: GetTokensFromDeviceUC,
    private val renewTokenRequestUC: RefreshTokenRequestUC,
    private val saveTokensAndCredentialsUC: SaveTokensAndCredentialsUC,
    private val encryptTokensAndCredentialsUC: EncryptTokensAndCredentialsUC,
    private val decryptTokensAndCredentialsUC: DecryptTokensAndCredentialsUC,
    private val logoutUC: LogoutUC,
    private val getCredentialsFromDeviceUC: GetCredentialsFromDeviceUC,
    private val registerPushUseCase: RegisterPushUseCase,
    private val getUserSettingsUC: GetUserSettingsUC,
    private val getIsFirstTimeUC: GetIsFirstTimeUC,
    private val getBiometricLoginStatusUC: GetBiometricLoginStatusUC,
    private val getUserInfoUC: GetUserInfoUC,
    private val saveUserInfoUC: SaveUserInfoUC,
    private val saveUserSettingsUC: SaveUserSettingsUC,
    private val saveAppUserFiltersUC: SaveAppUserFiltersUC
): ViewModel(), BiometricResponse {

    var state by mutableStateOf(LaunchScreenViewState.loading)
    var event = LaunchScreenViewEvent(viewModel = this)
    var errorMessage: String = ""

    var isEncrypting = false
    var pushNotificationEnvironment = "pre"

    var tokensAndCredentials = TokensAndCredentialsModel("", "", "", "")
    var userSettings:UserSettingsModel = UserSettingsModel()
    var userInfo: UserInfoModel = UserInfoModel()

    init {
        event.set(initialEvent)
    }

    override fun onBiometricSuccess(response: String?) {
        LoginViewModel.initialEvent = LoginViewEvent.doLogin
        event.set(navigateToLoginView)
    }

    override fun onBiometricFailure() {
        Log.d("LaunchScreenViewModel", "On biometric failure")
    }

    override fun onBiometricError(errCode: String, errResponse: String) {
        Log.d("LaunchScreenViewModel", "On biometric error")

        biometricLogin = false
        event.set(navigateToLoginViewAndShowLoginForm)
    }

    fun biometricLogin(mainActivity: FragmentActivity) {
        val biometricsApi = BiometricsApi(
            mainActivity = mainActivity,
            response = this
        )
        biometricsApi.showDialog()
    }

    override fun askUserForPermission() {
        Log.d("LaunchScreenViewModel", "Running askUserForPermission")
        event.set(showBiometricsSettings)
    }

    fun getCredentials() {
        viewModelScope.launch {
            getCredentialsFromDeviceUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { encryptedToken ->
                            encryptedString = encryptedToken
                            Log.d("LaunchScreenViewModel",
                                "getCredentials() -> Success: Existen tokens guardados: string de los tokens encriptados -> ${encryptedToken}")
                            event.set(getCredentialsSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result?.message.let { message ->
                            errorMessage = message!!
                            Log.d("LaunchScreenViewModel",
                                "getCredentials() -> Error: Ha ocurrido un error ${errorMessage}")
                            event.set(getCredentialsError)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun getTokensFromDevice() {

        viewModelScope.launch {
            getTokensFromDeviceUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { encryptedTokens ->
                            encryptedString = encryptedTokens
                            Log.d("LaunchScreenViewModel",
                                "getTokensFromDevice() -> Success: Existen tokens guardados: string de los tokens encriptados -> ${encryptedTokens}")
                            event.set(tokensExist)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result?.message.let { message ->
                            errorMessage = message!!
                            Log.d("LaunchScreenViewModel",
                                "getTokensFromDevice() -> Error: Ha ocurrido un error ${errorMessage}")
                            event.set(noTokens)
                        }
                    }
                }
            }
        }
    }

    fun renewTokenRequest(refreshToken: String) {

        viewModelScope.launch {
            renewTokenRequestUC.invoke(
                refreshToken = if (refreshToken.isEmpty()) " " else refreshToken
            ).collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { newTokens ->
                            Log.d("LaunchScreenViewModel",
                                "renewTokenRequest -> Success: Nuevo refresh token obtenido ${newTokens.refreshToken}")
                            IWinApi.ACCESS_TOKEN = newTokens.accessToken
                            IWinApi.REFRESH_TOKEN = newTokens.refreshToken
                            event.set(refreshTokenRequestSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        errorMessage =
                            "renewTokenRequest() -> Error: Ha ocurrido un error: ${result.message}"
                        Log.d("LaunchScreenViewModel", errorMessage)
                        event.set(refreshTokenRequestError)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {

            logoutUC.invoke().collect { result ->

                when (result) {

                    is AsyncTaskResponse.Success -> {

                        result.data?.let { result ->
                            LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                            event.set(navigateToLoginView)
                        }
                    }

                    is AsyncTaskResponse.Error -> {

                        errorMessage = "Ha ocurrido un error " + result.message
                        event.set(showErrorMessage)
                    }
                }
            }
        }
    }

    fun decryptTokensAndCredentials(encryptedString: String) {

        viewModelScope.launch {

            decryptTokensAndCredentialsUC.invoke(encryptedString).collect { result ->
                when (result) {

                    is AsyncTaskResponse.Success -> {

                        result.data?.let { result ->
                            Log.d("LaunchScreenViewModel",
                                "decryptTokensAndCredentials() -> Success: " + result)
                            tokensAndCredentials = result.toTokensAndCredentialsModel()
                            IWinApi.USERNAME = tokensAndCredentials.username
                            IWinApi.PASSWORD = tokensAndCredentials.password
                            IWinApi.ACCESS_TOKEN = tokensAndCredentials.accessToken
                            IWinApi.REFRESH_TOKEN = tokensAndCredentials.refreshToken

                            event.set(tokensAndCredentialsDecrypted)
                        }
                    }

                    is AsyncTaskResponse.Error -> {
                        when (result.data) {
                            "notAuthenticated" -> {
                                event.set(tokensAndCredentialsNotDecrypted)
                                errorMessage =
                                    "decryptTokensAndCredentials() -> Ha ocurrido un error: " + result.message
                                Log.d("LaunchScreenViewModel", errorMessage)
                            }
                            "ivIncorrect" -> {
                                event.set(biometricsDecryptionError)
                                errorMessage =
                                    "decryptTokensAndCredentials() -> Ha ocurrido un error: " + result.message
                                Log.d("LaunchScreenViewModel", errorMessage)
                            }
                            "cipherNotInitialized" -> {
                                event.set(biometricsDecryptionError)
                            }
                        }
                    }
                }
            }
        }
    }

    fun encryptTokensAndCredentials(stringToEncrypt: String) {

        viewModelScope.launch { 

            encryptTokensAndCredentialsUC.invoke(stringToEncrypt).collect { result ->
                when (result) {

                    is AsyncTaskResponse.Success -> {

                        result.data?.let { result ->
                            Log.d("LaunchScreenViewModel",
                                "encryptTokensAndUsername() -> Success: " + result)
                            encryptedString = result
                            event.set(tokensAndCredentialsEncrypted)
                        }
                    }

                    is AsyncTaskResponse.Error -> {
                        when (result.data) {
                            "notAuthenticated" -> {
                                event.set(tokensAndCredentialsNotEncrypted)
                                errorMessage =
                                    "encryptTokensAndUsername() -> Ha ocurrido un error: " + result.message
                                Log.d("LaunchScreenViewModel", errorMessage)
                            }
                            "ivIncorrect" -> {
                                event.set(biometricsEncryptionError)
                                errorMessage =
                                    "encryptTokensAndUsername() -> Ha ocurrido un error: " + result.message
                                Log.d("LaunchScreenViewModel", errorMessage)
                            }

                            "cipherNotInitialized" -> {
                                event.set(biometricsEncryptionError)
                            }
                        }
                    }
                }
            }
        }
    }

    fun saveEncryptedTokensToDevice(value: String) {
        Log.d("LaunchScreenViewModel", "encryptedToken variable is $encryptedString")
        viewModelScope.launch {
            saveTokensAndCredentialsUC.invoke(value).collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.d("LaunchScreenViewModel",
                                "saveEncryptedTokensToDevice() -> Success: " + result)
                            event.set(tokensSavedOnDevice)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        val message =
                            "saveEncryptedTokensToDevice() -> Ha ocurrido un error: " + result.message
                        errorMessage = message
                        Log.d("LaunchScreenViewModel", errorMessage)
                        event.set(tokensNotSavedOnDevice)
                    }
                }
            }
        }
    }

    fun registerPushOn(environment: String, activity: Activity) {
        showError("Registrando...")
        pushNotificationEnvironment = environment

        val username = IWinApi.USERNAME
        var pushUrl = ""

        if (environment == "PRE") {
            pushUrl = "https://push-pre.inditex.com:443"
        } else {
            pushUrl = "https://pushinet.inditex.com:443"
        }

        val pushServiceApi = PushServiceApi(
            activity = activity,
            alias = username,
            pushUrl = pushUrl,
            deviceKeyFromStorage = ""
        )

        pushServiceApi.register(
            onConnected = {
                Log.d("LaunchScreenViewModel","Push connected $it")
            },
            onConnectionFailed = {
                Log.d("LaunchScreenViewModel","Push not connected $it")
            }
        )

        event.set(registerToPushNotificationsSuccess)
    }

    fun unregisterPushOn(environment: String, activity: Activity) {
        //showError("Cancelando registro...")
        pushNotificationEnvironment = environment

        val username = IWinApi.USERNAME
        var pushUrl = ""

        if (environment == "PRE") {
            pushUrl = "https://push-pre.inditex.com:443"
        } else {
            pushUrl = "https://pushinet.inditex.com:443"
        }

        val pushServiceApi = PushServiceApi(
            activity = activity,
            alias = username,
            pushUrl = pushUrl,
            deviceKeyFromStorage = ""
        )

        pushServiceApi.delete(
            onSuccess = {
                //showError("Push unregistered")
                Log.d("LaunchScreenViewModel", "Push unregistered")
            },
             onError = {
                 //showError("Push not unregistered with Error: $it")
                 Log.d("LaunchScreenViewModel", "Push not unregistered with Error: $it")
             }
        )

        event.set(unregisterToPushNotificationsSuccess)
    }

    private fun showError(message: String) {
        errorMessage = message
        event.set(showErrorMessage)
    }

    fun getUserSettingsRequest() {
        viewModelScope.launch {
            getUserSettingsUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "getUserSettingsUC Success: ${result}")
                            userSettings = result
                            event.set(getUserSettingsRequestSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "getUserSettingsUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            event.set(getUserSettingsRequestError)
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            //event.set(tokensNotSavedOnDevice)
                        }

                    }
                }
            }
        }
    }

    fun getIsFirstTime() {
        viewModelScope.launch {
            getIsFirstTimeUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "getIsFirstTimeUC Success ${result}")
                            isFirstTime = result
                            event.set(isFirstTimeTrue)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "getIsFirstTimeUC() -> Ha ocurrido un error: " + errorMsg
                            errorMessage = message
                            event.set(isFirstTimeFalse)
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            //event.set(tokensNotSavedOnDevice)
                        }

                    }
                }
            }
        }
    }

    fun getBiometricLoginStatus() {
        viewModelScope.launch {
            getBiometricLoginStatusUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "getBiometricLoginStatusUC Success: ${result}")
                            biometricLogin = result
                            if (result) {
                                event.set(biometricLoginStatusTrue)
                            } else {
                                event.set(biometricLoginStatusFalse)
                            }
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "getBiometricLoginStatusUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            event.set(biometricLoginStatusFalse)
                        }
                    }
                }
            }
        }
    }

    fun authenticateToDecrypt(mainActivity: FragmentActivity) {
        val biometricsApi = BiometricsApi(
            mainActivity = mainActivity,
            secretKeyName = "tokensAndUsername",
            stringToEncrypt = "",
            encryptedString = encryptedString,
            response = this
        )
        biometricsApi.authenticateToDecrypt()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun authenticateToEncrypt(mainActivity: FragmentActivity) {
        val biometricsApi = BiometricsApi(
            mainActivity = mainActivity,
            secretKeyName = "tokensAndUsername",
            stringToEncrypt = stringToEncrypt,
            encryptedString = "",
            response = this
        )
        biometricsApi.authenticateToEncrypt()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            getUserInfoUC.invoke().collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "getUserInfoUC Success: ${result}")
                            userInfo = result
                            event.set(getUserInfoSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "getUserInfoUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            event.set(getUserInfoSuccess)
                        }
                    }
                }
            }
        }
    }

    fun saveUserInfo() {
        viewModelScope.launch {
            saveUserInfoUC.invoke(userInfo).collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "saveUserInfoUC Success: ${result}")
                            event.set(saveUserInfoSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "saveUserInfoUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            event.set(saveUserInfoError)
                        }
                    }
                }
            }
        }
    }

    fun saveUserSettings() {
        viewModelScope.launch {
            saveUserSettingsUC.invoke(userSettings).collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "saveUserSettingsUC Success: ${result}")
                            event.set(saveUserSettingsSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "saveUserSettingsUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            event.set(saveUserSettingsError)
                        }
                    }
                }
            }
        }
    }

    fun syncAppFiltersWithUserSettings() {

        AppFilters.activeIncidentsFilters.filterPriorityHigh = userSettings.visualizationSettings?.high
        AppFilters.followedIncidentsFilters.filterPriorityCritic = userSettings.visualizationSettings?.critique
        AppFilters.distributionCentersFilters.filterPriorityLow = userSettings.visualizationSettings?.low
        AppFilters.distributionCentersFilters.filterPriorityMid = userSettings.visualizationSettings?.medium

        val userAppFiltersModel = AppFilters.toUserAppFiltersModel()

        viewModelScope.launch {
            saveAppUserFiltersUC.invoke(userAppFiltersModel).collect { result ->
                when (result) {
                    is AsyncTaskResponse.Success -> {
                        result.data?.let { result ->
                            Log.i("LaunchScreenViewModel", "saveAppUserFiltersUC Success: ${result}")
                            event.set(syncAppFiltersWithUserSettingsSuccess)
                        }
                    }
                    is AsyncTaskResponse.Error -> {
                        result.data?.let { errorData ->

                        }

                        result.message?.let { errorMsg ->
                            val message =
                                "saveAppUserFiltersUC() -> Ha ocurrido un error: " + result.message
                            errorMessage = errorMsg
                            Log.d("LaunchScreenViewModel ", errorMessage)
                            event.set(syncAppFiltersWithUserSettingsError)
                        }
                    }
                }
            }
        }
    }

    fun onBiometricsSettingsResponse(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            // Error
            0 -> {
                event.set(navigateToLoginView)
            }
            // Success
            1 -> {
                Log.d("LaunchScreenViewModel", "Running onBiometricsSettingsResponse -> Success code 1")
                event.set(loginWithBiometrics)
            }
            3 -> {
                Log.d("LaunchScreenViewModel", "Running onBiometricsSettingsResponse -> Success code 3")
                event.set(loginWithBiometrics)
            }
        }
    }

    companion object {

        var initialEvent = getBiometricLoginStatus
        var encryptedString = ""
        var stringToEncrypt = ""
        var isFirstTime = false
        var biometricLogin = true
    }
}