package com.example.testwinkotlin.presentation.launchScreen

import android.util.Log
import com.example.testwinkotlin.data.remote.api.IWinApi
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel.Companion.biometricLogin
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel.Companion.encryptedString
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel.Companion.isFirstTime
import com.example.testwinkotlin.presentation.launchScreen.LaunchScreenViewModel.Companion.stringToEncrypt
import com.example.testwinkotlin.presentation.login.LoginViewEvent
import com.example.testwinkotlin.presentation.login.LoginViewModel

class LaunchScreenViewEvent
    constructor(
    var viewModel: LaunchScreenViewModel
) {

    fun set(event: LaunchScreenEvents) {

        Log.d("LaunchScreenViewModel", "Running event ${event}")
        viewModel.state = LaunchScreenViewState.loading

        when (event) {

            LaunchScreenEvents.getIsFirstTime -> {
                viewModel.getIsFirstTime()
            }
            LaunchScreenEvents.isFirstTimeTrue -> {
                isFirstTime = true
                set(LaunchScreenEvents.getTokensFromDevice)
            }
            LaunchScreenEvents.isFirstTimeFalse -> {
                isFirstTime = false
                set(LaunchScreenEvents.getBiometricLoginStatus)
            }


            LaunchScreenEvents.getBiometricLoginStatus -> {
                viewModel.getBiometricLoginStatus()
            }
            LaunchScreenEvents.biometricLoginStatusTrue -> {
                biometricLogin = true
                set(LaunchScreenEvents.getTokensFromDevice)
            }
            LaunchScreenEvents.biometricLoginStatusFalse -> {
                biometricLogin = false
                set(LaunchScreenEvents.getTokensFromDevice)
            }



            LaunchScreenEvents.getTokensFromDevice -> {
                viewModel.getTokensFromDevice()
            }
            LaunchScreenEvents.tokensExist -> {
                set(LaunchScreenEvents.decryptTokensAndCredentials)
            }
            LaunchScreenEvents.noTokens -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }



            LaunchScreenEvents.getCredentials -> {
                viewModel.getCredentials()
            }
            LaunchScreenEvents.getCredentialsSuccess -> {
                //set(LaunchScreenEvents.askBiometricAuthToDecrypt)
                set(LaunchScreenEvents.decryptTokensAndCredentials)
            }
            LaunchScreenEvents.getCredentialsError -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }



            LaunchScreenEvents.decryptTokensAndCredentials -> {
                viewModel.decryptTokensAndCredentials(encryptedString)
            }
            LaunchScreenEvents.tokensAndCredentialsDecrypted -> {
                set(LaunchScreenEvents.runRefreshTokenRequest)
            }
            LaunchScreenEvents.tokensAndCredentialsNotDecrypted -> {
                set(LaunchScreenEvents.askBiometricAuthToDecrypt)
            }



            LaunchScreenEvents.askBiometricAuthToDecrypt -> {
                viewModel.isEncrypting = false
                Log.d("LaunchScreenViewModel", "Encrypted tokens ${encryptedString}")
                viewModel.state = LaunchScreenViewState.showDecryptBiometricWindow
            }
            LaunchScreenEvents.biometricsDecryptionSuccess -> {
                set(LaunchScreenEvents.tokensAndCredentialsDecrypted)
            }
            LaunchScreenEvents.biometricsDecryptionError -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }



            LaunchScreenEvents.runRefreshTokenRequest -> {
                viewModel.renewTokenRequest(refreshToken = IWinApi.REFRESH_TOKEN)
            }
            LaunchScreenEvents.refreshTokenRequestError -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }
            LaunchScreenEvents.refreshTokenRequestSuccess -> {
                set(LaunchScreenEvents.getUserInfo)
            }



            LaunchScreenEvents.getUserInfo -> {
                viewModel.getUserInfo()
            }
            LaunchScreenEvents.getUserInfoSuccess -> {
                set(LaunchScreenEvents.saveUserInfo)
            }
            LaunchScreenEvents.getUserInfoError -> {

            }



            LaunchScreenEvents.saveUserInfo -> {
                viewModel.saveUserInfo()
            }
            LaunchScreenEvents.saveUserInfoSuccess -> {
                set(LaunchScreenEvents.registerToPushNotifications)
            }
            LaunchScreenEvents.saveUserInfoError -> {
            }



            LaunchScreenEvents.encryptTokensAndCredentials -> {
                viewModel.encryptTokensAndCredentials(stringToEncrypt)
            }
            LaunchScreenEvents.tokensAndCredentialsEncrypted -> {
                set(LaunchScreenEvents.saveEncryptedTokensToDevice)
            }
            LaunchScreenEvents.tokensAndCredentialsNotEncrypted -> {
                set(LaunchScreenEvents.showErrorMessage)
            }



            LaunchScreenEvents.askBiometricAuthToEncrypt -> {
                viewModel.isEncrypting = true
                Log.d("LaunchScreenViewModel", "String to encrypt is ${stringToEncrypt}")
                viewModel.state = LaunchScreenViewState.showEncryptBiometricWindow
            }
            LaunchScreenEvents.biometricsEncryptionSuccess -> {
                set(LaunchScreenEvents.tokensAndCredentialsEncrypted)
            }
            LaunchScreenEvents.biometricsEncryptionError -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }



            LaunchScreenEvents.saveEncryptedTokensToDevice -> {
                viewModel.saveEncryptedTokensToDevice(encryptedString)
            }
            LaunchScreenEvents.tokensSavedOnDevice -> {
                set(LaunchScreenEvents.registerToPushNotifications)
            }
            LaunchScreenEvents.tokensNotSavedOnDevice -> {
                viewModel.errorMessage = "Error al guardar las credenciales en el dispositivo"
                set(LaunchScreenEvents.showErrorMessage)
            }



            LaunchScreenEvents.registerToPushNotifications -> {
                viewModel.state = LaunchScreenViewState.registerToPushNotifications
            }
            LaunchScreenEvents.registerToPushNotificationsSuccess -> {
                set(LaunchScreenEvents.getUserSettingsRequest)
            }
            LaunchScreenEvents.registerToPushNotificationsError -> {
                viewModel.errorMessage = "Error al registrar las push notifications"
                set(LaunchScreenEvents.showErrorMessage)
            }



            LaunchScreenEvents.getUserSettingsRequest -> {
                viewModel.getUserSettingsRequest()
            }
            LaunchScreenEvents.getUserSettingsRequestSuccess -> {
                set(LaunchScreenEvents.saveUserSettings)
            }
            LaunchScreenEvents.getUserSettingsRequestError -> {
                set(LaunchScreenEvents.showErrorMessage)
            }



            LaunchScreenEvents.saveUserSettings -> {
                viewModel.saveUserSettings();
            }
            LaunchScreenEvents.saveUserSettingsSuccess -> {
                set(LaunchScreenEvents.syncAppFiltersWithUserSettings)
            }
            LaunchScreenEvents.saveUserSettingsError -> {
                set(LaunchScreenEvents.showErrorMessage)
            }




            LaunchScreenEvents.syncAppFiltersWithUserSettings -> {
                viewModel.syncAppFiltersWithUserSettings()
            }
            LaunchScreenEvents.syncAppFiltersWithUserSettingsSuccess -> {
                set(LaunchScreenEvents.navigateToAppView)
            }
            LaunchScreenEvents.syncAppFiltersWithUserSettingsError -> {
                set(LaunchScreenEvents.showErrorMessage)
            }




            LaunchScreenEvents.navigateToLoginView -> {
                viewModel.state = LaunchScreenViewState.navigateToLoginView
            }
            LaunchScreenEvents.navigateToAppView -> {
                viewModel.state = LaunchScreenViewState.navigateToAppView
            }




            LaunchScreenEvents.logout -> {
                set(LaunchScreenEvents.unregisterToPushNotifications)
            }




            LaunchScreenEvents.unregisterToPushNotifications -> {
                viewModel.state = LaunchScreenViewState.unregisterToPushNotifications
            }
            LaunchScreenEvents.unregisterToPushNotificationsSuccess -> {
                set(LaunchScreenEvents.janusLogout)
            }
            LaunchScreenEvents.unregisterToPushNotificationsError -> {
                set(LaunchScreenEvents.showErrorMessage)
            }




            LaunchScreenEvents.janusLogout -> {
                viewModel.logout()
            }
            LaunchScreenEvents.janusLogoutSuccess -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }
            LaunchScreenEvents.janusLogoutError  -> {
                set(LaunchScreenEvents.showErrorMessage)
            }





            LaunchScreenEvents.showErrorMessage -> {
                viewModel.state = LaunchScreenViewState.error
            }




            LaunchScreenEvents.encryptWithoutAsk -> {
                viewModel.state = LaunchScreenViewState.encryptWithoutAsk
            }


            LaunchScreenEvents.loginWithBiometrics -> {
                viewModel.state = LaunchScreenViewState.loginWithBiometrics
            }
            LaunchScreenEvents.showBiometricsSettings -> {
                viewModel.state = LaunchScreenViewState.showBiometricsSettings
            }


            LaunchScreenEvents.biometricsSettingsError -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }
            LaunchScreenEvents.biometricsSettingsSuccess -> {
                set(LaunchScreenEvents.loginWithBiometrics)
                //viewModel.state = LaunchScreenViewState.showBiometricsSettings
            }
            LaunchScreenEvents.navigateToLoginViewAndShowLoginForm -> {
                LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
                set(LaunchScreenEvents.navigateToLoginView)
            }
        }
    }

    fun navigateToLoginAndShowLoginForm() {
        LoginViewModel.initialEvent = LoginViewEvent.showLoginForm
        set(LaunchScreenEvents.navigateToLoginView)
    }
}

enum class LaunchScreenEvents {
    getIsFirstTime,
    isFirstTimeTrue,
    isFirstTimeFalse,

    getBiometricLoginStatus,
    biometricLoginStatusTrue,
    biometricLoginStatusFalse,

    getTokensFromDevice,
    tokensExist,
    noTokens,

    runRefreshTokenRequest,
    refreshTokenRequestError,
    refreshTokenRequestSuccess,

    getUserInfo,
    getUserInfoSuccess,
    getUserInfoError,

    saveUserInfo,
    saveUserInfoSuccess,
    saveUserInfoError,

    askBiometricAuthToEncrypt,
    biometricsEncryptionSuccess,
    biometricsEncryptionError,
    encryptTokensAndCredentials,
    tokensAndCredentialsEncrypted,
    tokensAndCredentialsNotEncrypted,

    askBiometricAuthToDecrypt,
    biometricsDecryptionSuccess,
    biometricsDecryptionError,
    decryptTokensAndCredentials,
    tokensAndCredentialsDecrypted,
    tokensAndCredentialsNotDecrypted,

    saveEncryptedTokensToDevice,
    tokensSavedOnDevice,
    tokensNotSavedOnDevice,

    getCredentials,
    getCredentialsSuccess,
    getCredentialsError,

    registerToPushNotifications,
    registerToPushNotificationsSuccess,
    registerToPushNotificationsError,

    logout,
    janusLogout,
    janusLogoutSuccess,
    janusLogoutError,

    unregisterToPushNotifications,
    unregisterToPushNotificationsSuccess,
    unregisterToPushNotificationsError,

    navigateToAppView,
    navigateToLoginView,

    navigateToLoginViewAndShowLoginForm,

    showErrorMessage,

    encryptWithoutAsk,
    getUserSettingsRequest,
    getUserSettingsRequestSuccess,
    getUserSettingsRequestError,

    saveUserSettings,
    saveUserSettingsSuccess,
    saveUserSettingsError,

    syncAppFiltersWithUserSettings,
    syncAppFiltersWithUserSettingsSuccess,
    syncAppFiltersWithUserSettingsError,

    showBiometricsSettings,
    loginWithBiometrics,

    biometricsSettingsError,
    biometricsSettingsSuccess
}